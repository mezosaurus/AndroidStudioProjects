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
import java.util.Set;
import java.util.UUID;

/**
 * Created by Ethan on 10/29/2014.
 */
public class GameListFragment extends Fragment implements ListAdapter {

    ArrayList<UUID> mGameIdentifiersByDate = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView gameListView = new ListView(getActivity());
        gameListView.setAdapter(this);

        gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UUID gameIdentifier = mGameIdentifiersByDate.get(position);

                if (mOnGameSelectedListener != null) {
                    mOnGameSelectedListener.onGameSelected(GameListFragment.this, gameIdentifier);
                }
            }
        });

        return gameListView;
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
            Set<UUID> gameIdentifiers = GameList.getInstance().getIdentifiers();
            mGameIdentifiersByDate = new ArrayList<UUID>();
            mGameIdentifiersByDate.addAll(gameIdentifiers);
            Collections.sort(mGameIdentifiersByDate);
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
        UUID gameIdentifier = mGameIdentifiersByDate.get((int) getItemId(position));
        Game game = GameList.getInstance().getGame(gameIdentifier);

        TextView gameTitleView = new TextView(getActivity());
        gameTitleView.setText(game.date.toString());
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
