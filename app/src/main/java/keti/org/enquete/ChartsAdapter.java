package keti.org.enquete;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.HashMap;

import keti.org.enquete.model.Question;
import keti.org.enquete.model.Survey;

/**
 * Created by Jaewook on 2015-04-09.
 */
public class ChartsAdapter extends BaseAdapter {
    Typeface tf;
    private ArrayList<Survey> list;
    ArrayList<String> heartAnswer;
    ArrayList<String> smileAnswer;
    ArrayList<Question> questions;

    ViewHolder viewHolder;
    LayoutInflater inflater;

    public ChartsAdapter(Context context, ArrayList<Survey> list, ArrayList<Question> questions) {
        this.list = list;
        this.questions = questions;
        heartAnswer = new ArrayList<>();
        smileAnswer = new ArrayList<>();


        heartAnswer.add("매우 좋음");
        heartAnswer.add("좋음");
        heartAnswer.add("보통");
        heartAnswer.add("나쁨");
        heartAnswer.add("매우 나쁨");

        smileAnswer.add("아니오");
        smileAnswer.add("예");

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Survey getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if ( view == null ) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.grid_item, null);
            viewHolder.questionTv = (TextView)view.findViewById(R.id.grid_item_questionTv);
            viewHolder.chart = (PieChart)view.findViewById(R.id.grid_item_pieChart);

            viewHolder.chart.setDescription("");

            viewHolder.chart.setCenterText("");
            viewHolder.chart.setCenterTextSize(18f);
            viewHolder.chart.setUsePercentValues(true);
            // radius of the center hole in percent of maximum radius
            viewHolder.chart.setHoleRadius(30f);
            viewHolder.chart.setTransparentCircleRadius(30f);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.questionTv.setText(questions.get(list.get(position).question - 1 ).question);
        viewHolder.chart.setData(getPieData(position));
        Legend l = viewHolder.chart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);

        return view;
    }

    class ViewHolder {
        TextView questionTv;
        PieChart chart;
    }

    public PieData getPieData(int position) {

        ArrayList<Entry> scores = new ArrayList<>();
        ArrayList<Integer> scoreList = list.get(position).scoreList;

        PieData pieData = null;

        if ( list.get(position).question == 8 ) {
            int[] score = new int[2];
            for ( int i = 0; i < scoreList.size(); i++ ) {
                score[scoreList.get(i)-1]++;
                smileAnswer.add("entry" + (i + 1));
            }
            for ( int i = 0; i < score.length; i++ ) {
                if ( score[i] > 0)
                    scores.add(new Entry((float) score[i], i));
            }
        } else {
            int[] score = new int[5];
            for ( int i = 0; i < scoreList.size(); i++ ) {
                score[scoreList.get(i)-1]++;
                heartAnswer.add("entry" + (i + 1));
            }
            for ( int i = 0; i < score.length; i++ ) {
                if ( score[i] > 0)
                    scores.add(new Entry((float) score[i], i));
            }
        }

        PieDataSet ds1 = new PieDataSet(scores, "");
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.GRAY);
        ds1.setValueTextSize(12f);
        ds1.setValueFormatter(new PercentFormatter());

        if ( list.get(position).question == 8 ) {
            pieData = new PieData(smileAnswer, ds1);
        } else
            pieData = new PieData(heartAnswer, ds1);
        return pieData;
    }
}
