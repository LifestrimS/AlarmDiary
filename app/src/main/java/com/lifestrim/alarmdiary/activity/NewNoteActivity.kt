package com.lifestrim.alarmdiary.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.jaredrummler.android.colorpicker.ColorShape
import com.lifestrim.alarmdiary.R
import com.lifestrim.alarmdiary.db.entity.Category
import com.lifestrim.alarmdiary.util.logd
import com.lifestrim.alarmdiary.viewmodel.CategoryViewModel
import kotlinx.android.synthetic.main.activity_new_note.*


class NewNoteActivity : AppCompatActivity(), ColorPickerDialogListener  {

    companion object {
        const val Extra_ID = "ExtraID"
        const val Extra_Title = "ExtraTitle"
        const val Extra_Text = "ExtraText"
        const val Extra_Category_Name = "ExtraCategoryName"
        const val Extra_Category_Color = "ExtraCategoryColor"
    }

    private lateinit var categoriesList : List<Category>
    private lateinit var categoryViewModel: CategoryViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        categoryViewModel.allCategories.observe(this, Observer<List<Category>> { categories ->
            categoryRadioGroup.removeAllViews()
            categoriesList = categories
            categories.forEach {
                val radioButton = RadioButton(this)
                radioButton.text = it.nameCategory
                it.colorCategory?.let { it1 -> radioButton.setTextColor(it1) }
                categoryRadioGroup.addView(radioButton)
            }

        })


        if (intent.hasExtra(Extra_ID)) {
            title = "Edit Note"
            edit_title.setText(intent.getStringExtra(Extra_Title))
            edit_text.setText(intent.getStringExtra(Extra_Text))
        } else {
            title = "Add Note"
        }

    }


    fun saveNote(view: View) {
        val title = edit_title.text.toString()
        val text = edit_text.text.toString()

        if (title.trim().isEmpty() || text.trim().isEmpty() || categoryRadioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(this, R.string.toast_title_and_text_empty, Toast.LENGTH_SHORT).show()
            return
        }
        val newIntent = Intent()
        newIntent.putExtra(Extra_Title, title)
        newIntent.putExtra(Extra_Text, text)

        val categoryChecked: RadioButton = findViewById(categoryRadioGroup.checkedRadioButtonId)

        newIntent.putExtra(Extra_Category_Name, categoryChecked.text.toString())
        newIntent.putExtra(Extra_Category_Color, categoryChecked.currentTextColor)



        val id = intent.getIntExtra(Extra_ID, -1)
        if (id != -1) {
            logd(id.toString())
            newIntent.putExtra(Extra_ID, id)
        }
        setResult(Activity.RESULT_OK, newIntent)
        finish()
    }

    fun addCategory(view: View) {
        if (categoryNameET.text.isEmpty()) {
            Toast.makeText(this, R.string.toast_category_name_empty, Toast.LENGTH_SHORT).show()
        } else {
            createColorPickerDialog()
        }
    }

    private fun createColorPickerDialog() {
        ColorPickerDialog.newBuilder()
            .setColor(Color.RED)
            .setDialogType(ColorPickerDialog.TYPE_PRESETS)
            .setAllowCustom(true)
            .setAllowPresets(true)
            .setColorShape(ColorShape.SQUARE)
            .setDialogId(1)
            .show(this)
    }

    override fun onDialogDismissed(dialogId: Int) {

    }

    private fun addCategoryToDB(nameCategory: String, colorCategory: Int) {
        val category = Category(nameCategory, colorCategory)
        if (findCategories(category) == -1) {
            categoryViewModel.categoryInsert(category)
            categoryNameET.setText("")
        } else {
            Toast.makeText(this, R.string.toast_category_already_exist, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onColorSelected(dialogId: Int, color: Int) {
        addCategoryToDB(categoryNameET.text.toString(), color)
    }

    private fun findCategories(category: Category): Int {
        categoriesList.forEach {
            if (it.nameCategory == category.nameCategory && it.colorCategory == category.colorCategory) {
                return it.id
            }
        }
        return -1
    }

    fun deleteCategory(view: View) {
        val radioButtonID = categoryRadioGroup.checkedRadioButtonId

        if(radioButtonID == -1)
        {
            Toast.makeText(this, R.string.toast_select_category_to_delete, Toast.LENGTH_SHORT).show()
        } else {
            val checkedCategoryRB:RadioButton = findViewById(radioButtonID)
            val checkedCategory = Category(checkedCategoryRB.text.toString(),checkedCategoryRB.currentTextColor)

            categoryViewModel.categoryDelete(findCategories(checkedCategory))
            Toast.makeText(this, "Category ${checkedCategory.nameCategory} deleted", Toast.LENGTH_SHORT).show()
        }
    }

}




