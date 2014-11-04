package cs4962.battleship;

import android.app.Fragment;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Ethan on 10/29/2014.
 */
public class GameListFragment extends Fragment implements ListAdapter {

    private ArrayList<UUID> mGameIdentifiersByDate = null;
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListView = new ListView(getActivity());
        mListView.setAdapter(this);

        GameList.getInstance().setGameListChange(new GameList.OnGameListChanged() {
            @Override
            public void onGameListChanged() {
                mListView.invalidateViews();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UUID gameIdentifier = mGameIdentifiersByDate.get(position);

                if (mOnGameSelectedListener != null) {
                    mOnGameSelectedListener.onGameSelected(GameListFragment.this, gameIdentifier);
                }
            }
        });

        return mListView;
    }

    public void refresh() {
        mListView.invalidateViews();
    }

    public interface OnGameSelectedListener {
        public void onGameSelected(GameListFragment gameListFragment, UUID identifier);
    }

    OnGameSelectedListener mOnGameSelectedListener = null;

    public OnGameSelectedListener getOnGameSelectedListener() {
        return mOnGameSelectedListener;
    }

    public void setOnGameSelectedListener(OnGameSelectedListener mOnGameSelectedListener) {
        this.mOnGameSelectedListener = mOnGameSelectedListener;
    }

    @Override
    public boolean isEmpty() {
        return getCount() <= 0;
    }

    @Override
    public int getCount() {
        if (mGameIdentifiersByDate == null) {
            mGameIdentifiersByDate = new ArrayList<UUID>();
            Set<UUID> gameIdentifiers = GameList.getInstance().getIdentifiers();
           mGameIdentifiersByDate.addAll(gameIdentifiers);
            // Sort by date
            Collections.sort(mGameIdentifiersByDate, new Comparator<UUID>() {
                public int compare (UUID one, UUID two) {
                    Date d1 = GameList.getInstance().getGame(one).getDate();
                    Date d2 = GameList.getInstance().getGame(two).getDate();
                    // newest date first
                    return d2.compareTo(d1);
                }
            });
        }
        return GameList.getInstance().getIdentifiers().size();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public Object getItem(int position) {
        return mGameIdentifiersByDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mGameIdentifiersByDate.clear();
        mGameIdentifiersByDate.addAll(GameList.getInstance().getIdentifiers());
        UUID gameIdentifier = mGameIdentifiersByDate.get((int) getItemId(position));
        Game game = GameList.getInstance().getGame(gameIdentifier);
        String itemTitleString = "";
        if (game.inProgress()) {
            itemTitleString += "IN PROGRESS\n";
            if (game.getPlayerOne().isTurn()) {
                itemTitleString += "P1 TURN\n";
            }
            else {
                itemTitleString += "P2 TURN\n";
            }
        }
        else {
            itemTitleString += "FINISHED\n";
        }

        itemTitleString += "P1 LAUNCHED: " + game.getPlayerOne().getActions().size() + "\n";
        itemTitleString += "P2 LAUNCHED: " + game.getPlayerTwo().getActions().size();

        TextView gameTitleView = new TextView(getActivity());
        gameTitleView.setText(itemTitleString);
        return gameTitleView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {}

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {}
}
