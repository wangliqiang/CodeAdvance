package com.app.codeadvance.indicator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.app.codeadvance.databinding.ActivityGradientTextBinding
import com.app.codeadvance.widget.GradientTextView

class GradientTextActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGradientTextBinding
    private val mTitles = arrayOf("关注", "首页", "热点", "推荐")
    private val mFragments: Array<TabFragment?> = arrayOfNulls(mTitles.size)
    private val mTabs = ArrayList<GradientTextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGradientTextBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initDatas()
        initEvents()
    }

    private fun initEvents() {
        for (item in mTabs.indices) {
            mTabs[item].setOnClickListener {
                binding.viewPager.currentItem = item
            }
        }
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (positionOffset > 0) {
                    val left: GradientTextView = mTabs[position]
                    val right: GradientTextView = mTabs[position + 1]

                    left.setDirection(GradientTextView.DIRECTION_RIGHT)
                    right.setDirection(GradientTextView.DIRECTION_LEFT)
                    left.setProgress(1 - positionOffset)
                    right.setProgress(positionOffset)
                }
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }

    private fun initDatas() {
        for (index in mTitles.indices) {
            mFragments[index] = TabFragment.newInstance(mTitles[index])
        }

        var mAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getItem(position: Int): Fragment {
                return mFragments[position]!!
            }
        }

        binding.viewPager.adapter = mAdapter
        binding.viewPager.currentItem = 0
        mTabs[binding.viewPager.currentItem].setProgress(1f)
    }

    private fun initViews() {
        mTabs.add(binding.focus)
        mTabs.add(binding.home)
        mTabs.add(binding.hot)
        mTabs.add(binding.recommend)
        binding.focus.setText("关注")
        binding.home.setText("首页")
        binding.hot.setText("热点")
        binding.recommend.setText("推荐")
    }
}