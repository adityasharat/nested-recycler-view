/*
 * Copyright (c) 2017 Aditya Sharat
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * opies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.jsvoid.nestedrecyclerview;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rvMain = (RecyclerView) findViewById(R.id.rv_main);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.setAdapter(new SimpleAdapter(true, 0, 0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

        static final Set<Integer> NESTED_RECYCLER_POSITIONS = new HashSet<>();

        static {
            NESTED_RECYCLER_POSITIONS.add(2);
            NESTED_RECYCLER_POSITIONS.add(7);
            NESTED_RECYCLER_POSITIONS.add(11);
            NESTED_RECYCLER_POSITIONS.add(18);
            NESTED_RECYCLER_POSITIONS.add(31);
        }

        public final boolean hasNesting;
        public int nestedPosition;
        public int count;

        public Map<Integer, Integer> map = new ArrayMap<>();

        public SimpleAdapter(boolean hasNesting, int nestedPosition, int count) {
            this.hasNesting = hasNesting;
            this.nestedPosition = nestedPosition;
            this.count = count;
        }

        @Override
        public SimpleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View view;
            if (type == 1) {
                RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.recycler_view, parent, false);
                rv.setLayoutManager(new LinearLayoutManager(parent.getContext()));
                rv.setAdapter(new SimpleAdapter(false, 0, 0));
                view = rv;
            } else {
                TextView textView = (TextView) inflater.inflate(R.layout.text_view, parent, false);
                if (this.hasNesting) {
                    textView.setText(String.format("The nestedPosition is %d", 0));
                } else {
                    textView.setText(String.format("The nestedPosition is %d - %d", this.nestedPosition, 0));
                }
                view = textView;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return new SimpleAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SimpleAdapter.ViewHolder holder, int position) {
            if (hasNesting && NESTED_RECYCLER_POSITIONS.contains(position)) {
                ((RecyclerView) holder.itemView).setAdapter(new SimpleAdapter(false, position, map.get(position)));
            } else {
                if (this.hasNesting) {
                    ((TextView) holder.itemView).setText(String.format("The nestedPosition is %d", position));
                } else {
                    ((TextView) holder.itemView).setText(String.format("The nestedPosition is %d - %d", this.nestedPosition, position));
                }
            }
        }

        @Override
        public int getItemCount() {
            if (hasNesting) {
                return 50;
            }
            return count;
        }

        @Override
        public int getItemViewType(int position) {
            if (!map.containsKey(position)) {
                map.put(position, (int) (Math.random() * 10));
            }
            return hasNesting && NESTED_RECYCLER_POSITIONS.contains(position) ? 1 : 0;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
