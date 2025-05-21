package com.example.app_capacitacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_capacitacion.Models.Course

class CursosDialogFragment : DialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var courseAdapter: CourseAdapter
    private val courseList: MutableList<Course> = mutableListOf()

    companion object {
        const val TAG = "CursosDialogFragment"
        fun newInstance(): CursosDialogFragment {
            return CursosDialogFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_cursos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewDialogCourses)
        recyclerView.layoutManager = LinearLayoutManager(context)

        courseAdapter = CourseAdapter(courseList)
        recyclerView.adapter = courseAdapter

        val dummyCourses = listOf(
            Course("Seguridad en Alturas", "2025-12-31"),
            Course("Primeros Auxilios Avanzados", "2026-06-15"),
            Course("Manejo de Extintores", "2025-10-01"),
            Course("Trabajo en Espacios Confinados", "2026-03-20"),
            Course("Trabajo en Espacios Confinados", "2026-03-20"),
            Course("Trabajo en Espacios Confinados", "2026-03-20"),
            Course("Trabajo en Espacios Confinados", "2026-03-20"),
            Course("Trabajo en Espacios Confinados", "2026-03-20")


        )

        courseAdapter.updateCourses(dummyCourses)

        val closeButton: Button = view.findViewById(R.id.buttonCloseDialog)
        closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            (resources.displayMetrics.heightPixels * 0.8).toInt()
        )
    }





}



