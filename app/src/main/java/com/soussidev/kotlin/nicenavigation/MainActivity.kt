package com.soussidev.kotlin.nicenavigation

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.soussidev.kotlin.navigationview.PaperFlower.PaperFlower
import com.soussidev.kotlin.navigationview.PaperFlower.PaperFlowerListener
import com.soussidev.kotlin.navigationview.navigation.NavigationView
import com.soussidev.kotlin.navigationview.navigation.OnNavigationItemClickListener

import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var paperflower: PaperFlower? = null
    private var viewPager: ViewPager? = null
    private var navigationView: NavigationView? = null
    private val image = intArrayOf(R.mipmap.icon_date, R.mipmap.icon_date, R.mipmap.icon_date)
    private val color = intArrayOf(R.color.menu_color_9, R.color.menu_color_1, R.color.menu_color_4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        paperflower = PaperFlower.attach2Window(this)

        navigationView = findViewById(R.id.navigationview) as NavigationView

        if (navigationView != null) {
            navigationView!!.isWithText(true)
            navigationView!!.disableShadow()
            navigationView!!.isColoredBackground(false)
            navigationView!!.setItemActiveColorWithoutColoredBackground(ContextCompat.getColor(this, R.color.menu_color_10))
        }

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        viewPager = findViewById(R.id.viewpager) as ViewPager
        setupViewPager(viewPager)



        navigationView!!.setUpWithViewPager(viewPager!!, color, image, this@MainActivity)
        navigationView!!.setOnBottomNavigationItemClickListener { index ->
            getflower(toolbar)
            //   navigationView.selectTab(index);
            val name = viewPager!!.adapter.getPageTitle(index).toString()

            toolbar.title = name
            toolbar.subtitle = "By soussidev"
            toolbar.setTitleTextColor(resources.getColor(R.color.white))
            toolbar.setSubtitleTextColor(resources.getColor(R.color.menu_color_1))
            toolbar.setLogo(R.mipmap.icon_fav)
        }


    }

    // Setup View Pager
    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        //change the fragmentName as per your need

        adapter.addFragment(PageFragment1(), "Page 1")
        adapter.addFragment(PageFragment2(), "Page 2")
        adapter.addFragment(PageFragment3(), "Page 3")

        viewPager!!.adapter = adapter
    }
    //View Pager Adaptateur

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }


    }


    //Paper Flower

    fun getflower(view: View) {
        paperflower!!.paperflower(view, 450f, object : PaperFlowerListener {
            override fun onAnimationStart() {

            }

            override fun onAnimationEnd() {

            }
        })
    }


    fun getlike(view: View) {
        paperflower!!.setColors(color)
        paperflower!!.paperflower(view)
        paperflower!!.setDotNumber(100)
        paperflower!!.setmListener(object : PaperFlowerListener {
            override fun onAnimationStart() {}

            override fun onAnimationEnd() {

            }
        })
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
