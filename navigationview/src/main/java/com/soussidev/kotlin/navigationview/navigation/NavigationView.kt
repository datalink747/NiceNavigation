package com.soussidev.kotlin.navigationview.navigation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.soussidev.kotlin.navigationview.PaperFlower.PaperFlower
import com.soussidev.kotlin.navigationview.R

import java.util.ArrayList

/**
 * Created by Soussi on 06/09/2017.
 */

class NavigationView : RelativeLayout {

    private var onBottomNavigationItemClickListener: OnNavigationItemClickListener? = null
    internal val context:Context
    private val NAVIGATION_HEIGHT = resources.getDimension(R.dimen.bottom_navigation_height).toInt()
    private val NAVIGATION_LINE_WIDTH = resources.getDimension(R.dimen.bottom_navigation_line_width).toInt()
    private var textActiveSize = resources.getDimension(R.dimen.bottom_navigation_text_size_active)
    private var textInactiveSize = resources.getDimension(R.dimen.bottom_navigation_text_size_inactive)
    private val navigationItems = ArrayList<NavigationItem>()
    private val viewList = ArrayList<View>()
    private var itemActiveColorWithoutColoredBackground = -1
    private var currentItem = 0
    private var navigationWidth: Int = 0
    private var shadowHeight: Int = 0
    private var itemInactiveColor: Int = 0
    private var itemWidth: Int = 0
    private var itemHeight: Int = 0
    private var withText = true
    private var coloredBackground = true
    private var disableShadow = false
    private var isTablet = false
    private var viewPagerSlide = true
    private var container: FrameLayout? = null
    private var backgroundColorTemp: View? = null
    private var mViewPager: ViewPager? = null
    private val paperflower: PaperFlower? = null

    constructor(context: Context) : super(context) {
        this.context = context

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.context = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.context = context
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        navigationWidth = NavigationUtils.getActionbarSize(context!!)
        val params = layoutParams
        if (coloredBackground) {
            itemActiveColorWithoutColoredBackground = ContextCompat.getColor(context!!, R.color.colorActive)
            itemInactiveColor = ContextCompat.getColor(context!!, R.color.colorInactive)
            shadowHeight = resources.getDimension(R.dimen.bottom_navigation_shadow_height).toInt()
        } else {
            if (itemActiveColorWithoutColoredBackground == -1)
                itemActiveColorWithoutColoredBackground = ContextCompat.getColor(context!!, R.color.itemActiveColorWithoutColoredBackground)
            itemInactiveColor = ContextCompat.getColor(context!!, R.color.withoutColoredBackground)
            shadowHeight = resources.getDimension(R.dimen.bottom_navigation_shadow_height_without_colored_background).toInt()
        }
        if (isTablet) {
            params.width = navigationWidth + NAVIGATION_LINE_WIDTH
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = if (disableShadow) NAVIGATION_HEIGHT else NAVIGATION_HEIGHT + shadowHeight
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                elevation = resources.getDimension(R.dimen.bottom_navigation_elevation)
            }
        }
        layoutParams = params
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (currentItem < 0 || currentItem > navigationItems.size - 1) {
            throw IndexOutOfBoundsException(if (currentItem < 0)
                "Position must be 0 or greater than 0, current is " + currentItem
            else
                "Position must be less or equivalent than items size, items size is " + (navigationItems.size - 1) + " current is " + currentItem)
        }
        if (navigationItems.size == 0) {
            throw NullPointerException("You need at least one item")
        }
        val containerParams: RelativeLayout.LayoutParams
        val params: RelativeLayout.LayoutParams
        val lineParams: RelativeLayout.LayoutParams
        val white = ContextCompat.getColor(context!!, R.color.white)
        backgroundColorTemp = View(context)
        viewList.clear()
        if (isTablet) {
            itemWidth = RelativeLayout.LayoutParams.MATCH_PARENT
            itemHeight = navigationWidth
        } else {
            itemWidth = width / navigationItems.size
            itemHeight = RelativeLayout.LayoutParams.MATCH_PARENT
        }
        container = FrameLayout(context!!)
        val shadow = View(context)
        val line = View(context)
        val items = LinearLayout(context)
        items.orientation = if (isTablet) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL
        val shadowParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, shadowHeight)
        if (isTablet) {
            line.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorInactive))
            containerParams = RelativeLayout.LayoutParams(navigationWidth, ViewGroup.LayoutParams.MATCH_PARENT)
            lineParams = RelativeLayout.LayoutParams(NAVIGATION_LINE_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT)
            lineParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            params = RelativeLayout.LayoutParams(navigationWidth, ViewGroup.LayoutParams.MATCH_PARENT)
            items.setPadding(0, itemHeight / 2, 0, 0)
            addView(line, lineParams)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val backgroundLayoutParams = RelativeLayout.LayoutParams(
                        navigationWidth, ViewGroup.LayoutParams.MATCH_PARENT)
                backgroundLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                container!!.addView(backgroundColorTemp, backgroundLayoutParams)
            }
        } else {
            params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, NAVIGATION_HEIGHT)
            containerParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, NAVIGATION_HEIGHT)
            shadowParams.addRule(RelativeLayout.ABOVE, container!!.id)
            shadow.setBackgroundResource(R.drawable.shadow)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val backgroundLayoutParams = RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, NAVIGATION_HEIGHT)
                backgroundLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                container!!.addView(backgroundColorTemp, backgroundLayoutParams)
            }
        }
        containerParams.addRule(if (isTablet) RelativeLayout.ALIGN_PARENT_LEFT else RelativeLayout.ALIGN_PARENT_BOTTOM)
        addView(shadow, shadowParams)
        addView(container, containerParams)
        container!!.addView(items, params)
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        for (i in navigationItems.indices) {
            if (!coloredBackground)
                navigationItems[i].color = white
            val textActivePaddingTop = context!!.resources.getDimension(R.dimen.bottom_navigation_padding_top_active).toInt()
            val viewInactivePaddingTop = context!!.resources.getDimension(R.dimen.bottom_navigation_padding_top_inactive).toInt()
            val viewInactivePaddingTopWithoutText = context!!.resources.getDimension(R.dimen.bottom_navigation_padding_top_inactive_without_text).toInt()
            val view = inflater.inflate(R.layout.bottom_navigation, this, false)
            val icon = view.findViewById<View>(R.id.navigation_item_icon) as ImageView
            val title = view.findViewById<View>(R.id.navigation_item_title) as TextView
            if (isTablet)
                title.visibility = View.GONE
            title.setTextColor(itemInactiveColor)
            viewList.add(view)
            icon.setImageResource(navigationItems[i].imageResource)
            icon.setColorFilter(if (i == currentItem) itemActiveColorWithoutColoredBackground else itemInactiveColor)
            if (i == currentItem) {
                container!!.setBackgroundColor(navigationItems[i].color)
                title.setTextColor(if (currentItem == i)
                    itemActiveColorWithoutColoredBackground
                else
                    itemInactiveColor)
                icon.scaleX = 1.1.toFloat()
                icon.scaleY = 1.1.toFloat()
            }
            if (isTablet)
                view.setPadding(view.paddingLeft, view.paddingTop, if (i == currentItem) textActivePaddingTop else if (withText) viewInactivePaddingTop else viewInactivePaddingTopWithoutText,
                        view.paddingBottom)
            else
                view.setPadding(view.paddingLeft, if (i == currentItem) textActivePaddingTop else if (withText) viewInactivePaddingTop else viewInactivePaddingTopWithoutText, view.paddingRight,
                        view.paddingBottom)
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, if (i == currentItem)
                textActiveSize
            else if (withText) textInactiveSize else 0F)
            title.text = navigationItems[i].title
            val itemParams = RelativeLayout.LayoutParams(itemWidth, itemHeight)
            items.addView(view, itemParams)
            view.setOnClickListener { onBottomNavigationItemClick(i) }
        }

    }

    /**
     * @auteur Soussi Mohamed .
     */

    private fun onBottomNavigationItemClick(itemIndex: Int) {


        if (currentItem == itemIndex) {
            return
        }

        val viewActivePaddingTop = context!!.resources.getDimension(R.dimen.bottom_navigation_padding_top_active).toInt()
        val viewInactivePaddingTop = context!!.resources.getDimension(R.dimen.bottom_navigation_padding_top_inactive).toInt()
        val viewInactivePaddingTopWithoutText = context!!.resources.getDimension(R.dimen.bottom_navigation_padding_top_inactive_without_text).toInt()
        var centerX: Int
        var centerY: Int
        for (i in viewList.indices) {
            if (i == itemIndex) {
                val view = viewList[itemIndex].findViewById<View>(R.id.bottom_navigation_container)

                val title = view.findViewById<View>(R.id.navigation_item_title) as TextView
                val icon = view.findViewById<View>(R.id.navigation_item_icon) as ImageView
                NavigationUtils.changeTextColor(title, itemInactiveColor, itemActiveColorWithoutColoredBackground)
                NavigationUtils.changeTextSize(title, if (withText) textInactiveSize else 0F, textActiveSize)
                NavigationUtils.imageColorChange(icon, itemInactiveColor, itemActiveColorWithoutColoredBackground)
                if (isTablet)
                    NavigationUtils.changeRightPadding(view, if (withText) viewInactivePaddingTop else viewInactivePaddingTopWithoutText, viewActivePaddingTop)
                else
                    NavigationUtils.changeTopPadding(view, if (withText) viewInactivePaddingTop else viewInactivePaddingTopWithoutText, viewActivePaddingTop)
                icon.animate()
                        .setDuration(150)
                        .scaleX(1.1.toFloat())
                        .scaleY(1.1.toFloat())
                        .start()
                if (isTablet) {
                    centerX = viewList[itemIndex].width / 2
                    centerY = viewList[itemIndex].y.toInt() + viewList[itemIndex].height / 2
                } else {
                    centerX = viewList[itemIndex].x.toInt() + viewList[itemIndex].width / 2
                    centerY = viewList[itemIndex].height / 2
                }
                val finalRadius = Math.max(width, height)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    backgroundColorTemp!!.setBackgroundColor(navigationItems[itemIndex].color)
                    val changeBackgroundColor = ViewAnimationUtils.createCircularReveal(backgroundColorTemp, centerX, centerY, 0f, finalRadius.toFloat())
                    changeBackgroundColor.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            container!!.setBackgroundColor(navigationItems[itemIndex].color)
                        }
                    })
                    changeBackgroundColor.start()
                } else {
                    NavigationUtils.backgroundColorChange(container!!, navigationItems[currentItem].color, navigationItems[itemIndex].color)
                }
            } else if (i == currentItem) {
                val view = viewList[i].findViewById<View>(R.id.bottom_navigation_container)
                val title = view.findViewById<View>(R.id.navigation_item_title) as TextView
                val icon = view.findViewById<View>(R.id.navigation_item_icon) as ImageView
                NavigationUtils.imageColorChange(icon, itemActiveColorWithoutColoredBackground, itemInactiveColor)
                NavigationUtils.changeTextColor(title, itemActiveColorWithoutColoredBackground, itemInactiveColor)
                NavigationUtils.changeTextSize(title, textActiveSize, if (withText) textInactiveSize else 0F)
                if (isTablet)
                    NavigationUtils.changeRightPadding(view, viewActivePaddingTop, if (withText) viewInactivePaddingTop else viewInactivePaddingTopWithoutText)
                else
                    NavigationUtils.changeTopPadding(view, viewActivePaddingTop, if (withText) viewInactivePaddingTop else viewInactivePaddingTopWithoutText)
                icon.animate()
                        .setDuration(150)
                        .scaleX(1f)
                        .scaleY(1f)
                        .start()
            }
        }

        if (mViewPager != null)
            mViewPager!!.setCurrentItem(itemIndex, viewPagerSlide)

        if (onBottomNavigationItemClickListener != null)
            onBottomNavigationItemClickListener!!.onNavigationItemClick(itemIndex)
        currentItem = itemIndex
    }

    /**
     * Creates a connection between this navigation view and a ViewPager
     *
     * @param pager          pager to connect to
     * @param colorResources color resources for every item in the ViewPager adapter
     * @param imageResources images resources for every item in the ViewPager adapter
     */

    /**
     * @auteur Soussi Mohamed .
     */

    fun setUpWithViewPager(pager: ViewPager, colorResources: IntArray, imageResources: IntArray, ct: Context) {
        // paperflower = PaperFlower.attach2Window(ct);
        this.mViewPager = pager
        if (pager.adapter.count != colorResources.size || pager.adapter.count != imageResources.size)
            throw IllegalArgumentException("colorResources and imageResources must be equal to the ViewPager items : " + pager.adapter.count)

        for (i in 0..pager.adapter.count - 1)
            addTab(NavigationItem(pager.adapter.getPageTitle(i).toString(), colorResources[i], imageResources[i]))

        mViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //  selectTab(position);
            }

            override fun onPageSelected(position: Int) {
                selectTab(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    /**
     * Add item for Navigation
     *
     * @param item item to add
     */
    /**
     * @auteur Soussi Mohamed .
     */
    fun addTab(item: NavigationItem) {
        navigationItems.add(item)
    }

    /**
     * Activate Navigation tablet mode
     */
    fun activateTabletMode() {
        isTablet = true
    }

    /**
     * Change text visibility
     *
     * @param withText disable or enable item text
     */
    fun isWithText(withText: Boolean) {
        this.withText = withText
    }

    /**
     * Item Active Color if isColoredBackground(false)
     *
     * @param itemActiveColorWithoutColoredBackground active item color
     */
    fun setItemActiveColorWithoutColoredBackground(itemActiveColorWithoutColoredBackground: Int) {
        this.itemActiveColorWithoutColoredBackground = itemActiveColorWithoutColoredBackground
    }

    /**
     * With this Navigation background will be white
     *
     * @param coloredBackground disable or enable background color
     */
    fun isColoredBackground(coloredBackground: Boolean) {
        this.coloredBackground = coloredBackground
    }

    /**
     * Change tab programmatically
     *
     * @param position selected tab position
     */
    fun selectTab(position: Int) {
        onBottomNavigationItemClick(position)
        currentItem = position
    }

    /**
     * Disable shadow of NavigationView
     */
    fun disableShadow() {
        disableShadow = true
    }

    /**
     * Disable slide animation when using ViewPager
     */
    fun disableViewPagerSlide() {
        viewPagerSlide = false
    }

    /**
     * Change Active text size
     *
     * @param textActiveSize size
     */
    fun setTextActiveSize(textActiveSize: Float) {
        this.textActiveSize = textActiveSize
    }

    /**
     * Change Inactive text size
     *
     * @param textInactiveSize size
     */
    fun setTextInactiveSize(textInactiveSize: Float) {
        this.textInactiveSize = textInactiveSize
    }

    /**
     * Setup interface for item onClick
     */
    /**
     * @auteur Soussi Mohamed .
     */
    fun setOnBottomNavigationItemClickListener(onBottomNavigationItemClickListener: OnNavigationItemClickListener) {
        this.onBottomNavigationItemClickListener = onBottomNavigationItemClickListener
    }
}

