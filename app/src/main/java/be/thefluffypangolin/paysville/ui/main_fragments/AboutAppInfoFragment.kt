package be.thefluffypangolin.paysville.ui.main_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import be.thefluffypangolin.paysville.databinding.LayoutAppInfoBinding

class AboutAppInfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return LayoutAppInfoBinding.inflate(inflater, container, false).root
    }
}