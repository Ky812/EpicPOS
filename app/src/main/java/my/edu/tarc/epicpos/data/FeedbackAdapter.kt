package my.edu.tarc.epicpos.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import my.edu.tarc.epicpos.R

class FeedbackAdapter (private val feedbackList : List<Feedback>) : RecyclerView.Adapter<FeedbackAdapter.feedbackViewHolder> () {
    class feedbackViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val rating : RatingBar = itemView.findViewById(R.id.ratingBar2)
        val comment : TextView = itemView.findViewById(R.id.tvCustFeedback)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): feedbackViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feedback,parent,false)

        return feedbackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: feedbackViewHolder, position: Int) {
        val currentPosition = feedbackList[position]
        holder.rating.rating = currentPosition.rating.toFloat()
        holder.comment.text = "Comment : ${currentPosition.comment}"
    }

    override fun getItemCount(): Int {
        return feedbackList.size
    }
}