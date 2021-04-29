package co.dekhok.railway;

/**
 * Created by dekho on 01/07/2019.
 */

public class Question_Adapteru {/*extends RecyclerView.Adapter<Question_Adapteru.ViewHolder>{

    private ArrayList<Model> data;
    Context context;

    public Question_Adapteru(ArrayList<Model> data, Context context) {
        this.data = data;
        this.context = context;
    }
    @Override
    public Question_Adapteru.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_adapter, viewGroup, false);
        return new Question_Adapteru.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final Question_Adapteru.ViewHolder viewHolder, int i){
        final Model abc=data.get(i);
        viewHolder.question.setText(abc.getQuestion());

    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView question;
        Spinner spinner;
        RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            question=(TextView)view.findViewById(R.id.question);
            spinner=(Spinner)view.findViewById(R.id.spinnerMulti);
        }
    }*/
}
