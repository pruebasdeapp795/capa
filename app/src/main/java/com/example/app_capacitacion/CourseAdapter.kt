package com.example.app_capacitacion

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.app_capacitacion.Models.Course
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class CourseAdapter (
    private val courses: MutableList<Course>
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseName: TextView = itemView.findViewById(R.id.textViewCourseName)
        val expirationDate: TextView = itemView.findViewById(R.id.textViewExpirationDate)
        val courseStatus: TextView = itemView.findViewById(R.id.textViewCourseStatus)
        val courseBackground: LinearLayout = itemView.findViewById(R.id.course_status_background)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Formato de entrada desde Laravel
        val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Formato de salida deseado

        try {
            val finalDateParsed = inputDateFormat.parse(course.fecha_final)
            if (finalDateParsed != null) {
                holder.expirationDate.text = outputDateFormat.format(finalDateParsed) // Formatea la fecha para mostrar
            } else {
                holder.expirationDate.text = "Fecha inválida"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            holder.expirationDate.text = "Error de formato" // Otro manejo de error
        }


        holder.courseName.text = course.nombre
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Asegúrate de que el formato coincida con Laravel
        val today = Calendar.getInstance().time // Fecha y hora actuales
        var calculatedStatus: String = "Desconocido"
        val context = holder.itemView.context

        try {
            val finalDate = dateFormat.parse(course.fecha_final)
            val startDate = dateFormat.parse(course.fecha_inicio) // Asumiendo que 'fecha_inicio' viene de Laravel

            if (finalDate != null && startDate != null) {
                // Cálculo de días restantes
                val diffInMillis = finalDate.time - today.time
                val daysRemaining = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS).toInt()

                // Lógica de estados basada en los requisitos
                if (today.after(finalDate)) {
                    // Si la fecha actual es posterior a la fecha final, está Finalizado
                    calculatedStatus = "Finalizado"
                } else if (today.before(startDate)) {
                    // Si la fecha actual es anterior a la fecha de inicio, está Pendiente
                    calculatedStatus = "Pendiente"
                } else if (daysRemaining <= 30) { // Menos o igual a 30 días restantes
                    calculatedStatus = "Pronto a finalizar"
                } else { // Más de 30 días restantes
                    calculatedStatus = "Con tiempo"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            calculatedStatus = "Error de Fecha" // Manejo de errores
        }

        holder.courseStatus.text = "Estado: $calculatedStatus"

        // Aplicar colores basados en el estado calculado
        when (calculatedStatus) {
            "Finalizado" -> {
                holder.courseBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.course_finalized))
                holder.courseStatus.setTextColor(ContextCompat.getColor(context, R.color.white)) // Texto blanco para mejor contraste
            }
            "Pronto a finalizar" -> {
                holder.courseBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.course_soon_to_end))
                holder.courseStatus.setTextColor(ContextCompat.getColor(context, R.color.black)) // Texto negro para mejor contraste
            }
            "Con tiempo" -> {
                holder.courseBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.course_with_time))
                holder.courseStatus.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            "Pendiente" -> {
                holder.courseBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.course_pending))
                holder.courseStatus.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            else -> {
                // Color por defecto si el estado es desconocido o hay un error
                holder.courseBackground.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                holder.courseStatus.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    fun updateCourses(newCourses: List<Course>) {
        courses.clear()
        courses.addAll(newCourses)
        notifyDataSetChanged()
    }
}