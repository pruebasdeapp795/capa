package com.example.app_capacitacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_capacitacion.Models.Capacitante
import com.example.app_capacitacion.Models.Course
import java.io.Serializable

class CursosDialogFragment : DialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var courseAdapter: CourseAdapter
    // Ahora courseList se inicializará con los datos del Bundle
    private var courseList: MutableList<Course> = mutableListOf()

    companion object {
        const val TAG = "CursosDialogFragment"
        private const val ARG_COURSES = "courses_list" // Clave para el Bundle

        // Cambiamos newInstance para que reciba una lista de Capacitante.Curso
        fun newInstance(courses: List<Capacitante.Curso>): CursosDialogFragment {
            val fragment = CursosDialogFragment()
            val args = Bundle().apply {
                // Mapeamos Capacitante.Curso a tu modelo Course para el adaptador
                val simplifiedCourses = courses.map {
                    Course(nombre = it.nombre_curso, fecha_inicio = it.fecha_inicio, duracion_meses = it.duracion_meses, fecha_final = it.fecha_final)
                }
                // Asegúrate de que Course sea Serializable para esto
                putSerializable(ARG_COURSES, ArrayList(simplifiedCourses) as Serializable)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Recuperamos la lista de cursos del Bundle aquí
        arguments?.let {
            courseList = (it.getSerializable(ARG_COURSES) as? ArrayList<Course>)?.toMutableList() ?: mutableListOf()
        }
        // Aplica el estilo de diálogo completo si lo tienes definido
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_cursos, container, false) // Usa tu layout existente
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewDialogCourses)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Inicializamos el adaptador con la lista de cursos recuperada
        courseAdapter = CourseAdapter(courseList)
        recyclerView.adapter = courseAdapter

        // Ya no necesitamos los dummyCourses aquí, la lista se pasa desde DialogInfo
        // courseAdapter.updateCourses(dummyCourses) // Esto se elimina

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



