package co.dekhok.railway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

class Question_Adapter  extends RecyclerView.Adapter<Question_Adapter.ViewHolder>{

    private ArrayList<Model> data;
    Context context;

    public Question_Adapter(ArrayList<Model> data, Context context) {
        this.data = data;
        this.context = context;
    }
    @Override
    public Question_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_adapter, viewGroup, false);
        return new Question_Adapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final Question_Adapter.ViewHolder viewHolder, int i){
        final Model abc=data.get(i);
        viewHolder.question.setText(abc.getQuestion());
        viewHolder.one.setText(abc.ansr_one);
        viewHolder.two.setText(abc.two);
        viewHolder.three.setText(abc.three);
        viewHolder.four.setText(abc.four);

    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView question;
        RadioButton one,two,three,four;

        public ViewHolder(View view) {
            super(view);
            question=(TextView)view.findViewById(R.id.tvque);
            one=(RadioButton)view.findViewById(R.id.radioButton);
            two=(RadioButton)view.findViewById(R.id.radioButton2);
            three=(RadioButton)view.findViewById(R.id.radioButton3);
            four=(RadioButton)view.findViewById(R.id.radioButton4);
        }
    }
}

