package be.thefluffypangolin.paysville.ui.main_fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import android.widget.TextView
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import be.thefluffypangolin.paysville.R
import be.thefluffypangolin.paysville.databinding.FragmentMainAboutBinding

class AboutFragment : Fragment() {
    private lateinit var binding: FragmentMainAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainAboutBinding.inflate(inflater, container, false)
        return binding.root
    }
}