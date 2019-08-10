package ru.skillbranch.devintensive.ui.profile

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.LOG_TAG
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO set custom Theme this before super and setContentView

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()
        Log.d(LOG_TAG, "onCreate")
    }

    // Для api < 28 нет гарантии, что метод будет вызван - см. комментарии в видео : 1:57:45 (третье занятие)
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(IS_EDIT_MODE, isEditMode)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
    }

    private fun updateTheme(mode: Int) {
        Log.d(LOG_TAG, "updateTheme")
        // Плохая реализация, при его вызове происходит пересоздание activity
        // Обычно нужно тему устанавливать до вызова метода onCreate
        delegate.setLocalNightMode(mode)
    }

    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for ((k,v) in viewFields) {
                v.text = it[k].toString()
            }
        }
    }

    private fun initViews(savedInstanceState: Bundle?) {

        viewFields = mapOf(
            "firstName" to et_first_name,
            "lastName" to et_last_name,
            "about" to et_about,
            "repository" to et_repository,
            "rating" to tv_rating,
            "respect" to tv_respect,
            "nickname" to tv_nick_name,
            "rank" to tv_rank
        )

        // читаем значение из bundle, запихали его туда в onSaveInstanceState
        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        // устанавливаем currentMode в зависимости от того, какое значение установили из бандла
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (isEditMode) saveProfileInfo()
                isEditMode = !isEditMode
                showCurrentMode(isEditMode)
            }
        })

        btn_edit.setOnClickListener {
            if (isEditMode) saveProfileInfo()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }
    }

    private fun showCurrentMode(isEdit: Boolean) {

        // filter дает лямбду, в ней текущий элемент - it, собсна собрали сет и смотрим - есть ли в нем ключ текущий
        val info = viewFields.filter { setOf("firstName", "lastName", "about", "repository").contains(it.key) }

        // for ((k, v) - неиспользуемые элементы можем заменить нижним подчеркиванием
        for ((_, v) in info) {
            v as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
            v.background.alpha =  if (isEdit) 255 else 0
        }

        ic_eye.visibility = if (isEdit) View.GONE else View.VISIBLE
        // TextInputLayout - подсвечиваем кол-во символов, которое вводим в поле
        wr_about.isCounterEnabled = isEdit

        with(btn_edit) {
            val filter: ColorFilter? = if (isEdit) {
                PorterDuffColorFilter(
                    resources.getColor(R.color.color_accent, theme),
                    // режим наложения
                    PorterDuff.Mode.SRC_IN
                )
            } else {
                null
            }

            val icon = if (isEdit) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else {
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun saveProfileInfo() {
        Profile(
            firstName = et_first_name.text.toString(),
            lastName = et_last_name.text.toString(),
            about = et_about.text.toString(),
            repository = et_repository.text.toString()
        // apply - обратиться к только что созданному инстансу Profile
        ).apply {
            viewModel.saveProfileData(this)
        }

    }
}