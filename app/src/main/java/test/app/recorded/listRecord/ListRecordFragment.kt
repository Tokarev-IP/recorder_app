package test.app.recorded.listRecord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import test.app.recorded.R

/**
 * A simple [Fragment] subclass.
 * Use the [ListRecordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListRecordFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_record, container, false)
    }


}