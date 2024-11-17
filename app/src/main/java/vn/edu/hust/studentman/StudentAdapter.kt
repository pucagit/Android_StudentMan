package vn.edu.hust.studentman

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class StudentAdapter(
  private val students: MutableList<StudentModel>,
  private val context: Context
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

  class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
    val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
    val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
    val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(
      R.layout.layout_student_item,
      parent, false
    )
    return StudentViewHolder(itemView)
  }

  override fun getItemCount(): Int = students.size

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]

    holder.textStudentName.text = student.studentName
    holder.textStudentId.text = student.studentId

    // Set up click listener for editing
    holder.imageEdit.setOnClickListener {
      showEditDialog(position)
    }

    holder.imageRemove.setOnClickListener {
      showDeleteDialog(position)
    }
  }

  private fun showDeleteDialog(position: Int) {
    val student = students[position]

    AlertDialog.Builder(context).setTitle("Bạn có chắc muốn xóa sinh viên này?")
      .setPositiveButton("OK", { dialog, _ ->
          students.removeAt(position)
          notifyItemRemoved(position)
          dialog.dismiss()

        val rootView = (context as? MainActivity)?.findViewById<View>(android.R.id.content)
        rootView?.let {
          Snackbar.make(it, "${student.studentName} đã bị xóa!", Snackbar.LENGTH_LONG)
            .setAction("Hoàn tác") {
              students.add(student)
              notifyItemInserted(position)
            }
            .setBackgroundTint(ContextCompat.getColor(context, android.R.color.black))
            .show()
        }
      })
      .setNegativeButton("Cancel", {dialog,_ ->
        dialog.dismiss()
      }).create().show()
  }

  private fun showEditDialog(position: Int) {
    val student = students[position]
    val dialog = Dialog(context)
    dialog.setContentView(R.layout.layout_dialog)

    // Find and set current data to dialog fields
    val editName = dialog.findViewById<EditText>(R.id.name)
    val editMSSV = dialog.findViewById<EditText>(R.id.mssv)
    editName.setText(student.studentName)
    editMSSV.setText(student.studentId)

    // Set up buttons
    dialog.findViewById<Button>(R.id.okBtn).setOnClickListener {
      val newName = editName.text.toString()
      val newMSSV = editMSSV.text.toString()

      // Update student information
      student.studentName = newName
      student.studentId = newMSSV
      notifyItemChanged(position) // Notify the adapter about the change
      dialog.dismiss()
    }

    dialog.findViewById<Button>(R.id.cancelBtn).setOnClickListener {
      dialog.dismiss()
    }

    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    dialog.show()
  }
}
