package my.edu.tarc.epicpos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import my.edu.tarc.epicpos.data.Feedback
import my.edu.tarc.epicpos.data.FeedbackAdapter


class ViewFeedbackFragment : Fragment() {
    private lateinit var db: DatabaseReference
    private lateinit var feedbackRecyclerView: RecyclerView
    private lateinit var feedbackArrayList: ArrayList<Feedback>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedbackRecyclerView = view.findViewById(R.id.viewFeedbackRecyclerView)
        feedbackRecyclerView.layoutManager = LinearLayoutManager(this.context)
        feedbackRecyclerView.setHasFixedSize(true)
        feedbackArrayList = arrayListOf<Feedback>()

        getCustFeedback()
    }

    private fun getCustFeedback() {
        db = FirebaseDatabase.getInstance("https://fypproject-bdcb3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Feedback")

        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                feedbackArrayList.clear()
                if(snapshot.exists()){
                    for(snap in snapshot.children){
                        val eve = snap.getValue(Feedback::class.java)

                        feedbackArrayList.add(eve!!)
                    }
                    var adapter = FeedbackAdapter(feedbackArrayList)
                    feedbackRecyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}