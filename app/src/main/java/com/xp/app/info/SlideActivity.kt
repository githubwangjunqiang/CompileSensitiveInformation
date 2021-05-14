package com.xp.app.info

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.xp.app.info.slide.ItemConfig
import com.xp.app.info.slide.ItemTouchHelperCallback2
import com.xp.app.info.slide.OnSlideListener
import com.xp.app.info.slide.SlideLayoutManager
import java.util.*
import kotlin.system.exitProcess

class SlideActivity : AppCompatActivity() {
    private lateinit var mSlideLayoutManager: SlideLayoutManager
    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mItemTouchHelperCallback: ItemTouchHelperCallback2<String>
    public var mList: ArrayList<String> = ArrayList<String>()
    lateinit var mAdapter: MyAdapter

    private var mLikeCount = 50
    private var mDislikeCount = 50
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide)
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Log.e(TAG, "闪退: ", e)
            exitProcess(0)
        }
        val mRecyclerView = findViewById<RecyclerView>(R.id.recyleview)


        mAdapter = MyAdapter(this, mList)
        mRecyclerView.adapter = mAdapter
        addData()
        mItemTouchHelperCallback = ItemTouchHelperCallback2(mAdapter, mList)
        mItemTouchHelper = ItemTouchHelper(mItemTouchHelperCallback!!)
        mSlideLayoutManager = SlideLayoutManager(mRecyclerView, mItemTouchHelper)
        mItemTouchHelper.attachToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = mSlideLayoutManager





        mItemTouchHelperCallback.setOnSlideListener(object : OnSlideListener<String> {
            override fun onSliding(
                viewHolder: RecyclerView.ViewHolder?,
                ratio: Float, direction: Int
            ) {
                if (direction == ItemConfig.SLIDING_LEFT) {
                } else if (direction == ItemConfig.SLIDING_RIGHT) {
                }
            }

            override fun onSlided(
                viewHolder: RecyclerView.ViewHolder,
                string: String?,
                direction: Int
            ) {
                if (direction == ItemConfig.SLIDED_LEFT) {
                    mDislikeCount--
//                    mSmileView.setDisLike(mDislikeCount)
//                    mSmileView.disLikeAnimation()
                } else if (direction == ItemConfig.SLIDED_RIGHT) {
                    mLikeCount++
//                    mSmileView.setLike(mLikeCount)
//                    mSmileView.likeAnimation()
                }
                string?.run {
                    mList.add(this)
                }
                val position = viewHolder.adapterPosition
                Log.e(
                    "12345",
                    "onSlided--position:$position"
                )
            }

            override fun onClear() {
                addData()
            }

        })

    }

    /**
     * 向集合中添加数据
     */
    private fun addData() {
//        val icons = intArrayOf(
//            R.mipmap.header_icon_1, R.mipmap.header_icon_2, R.mipmap.header_icon_3,
//            R.mipmap.header_icon_4, R.mipmap.header_icon_1, R.mipmap.header_icon_2
//        )
//        val titles =
//            arrayOf("Acknowledging", "Belief", "Confidence", "Dreaming", "Happiness", "Confidence")
//        val says = arrayOf(
//            "Do one thing at a time, and do well.",
//            "Keep on going never give up.",
//            "Whatever is worth doing is worth doing well.",
//            "I can because i think i can.",
//            "Jack of all trades and master of none.",
//            "Keep on going never give up.",
//            "Whatever is worth doing is worth doing well."
//        )
//        val bgs = intArrayOf(
//            R.mipmap.img_slide_1,
//            R.mipmap.img_slide_2,
//            R.mipmap.img_slide_3,
//            R.mipmap.img_slide_4,
//            R.mipmap.img_slide_5,
//            R.mipmap.img_slide_6
//        )
        for (i in 0..12) {
            mList.add("$i")
        }
    }


    class MyAdapter(val context: Context, val list: ArrayList<String>) :
        RecyclerView.Adapter<MyVh>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVh {
            return MyVh(LayoutInflater.from(context).inflate(R.layout.item_rev, parent, false))
        }

        override fun onBindViewHolder(holder: MyVh, position: Int) {
            holder.tvText.text = list[position]
        }

        override fun getItemCount(): Int {
            return list.size
        }

    }

    class MyVh(val view: View) : RecyclerView.ViewHolder(view) {

        val tvText = view.findViewById<TextView>(R.id.tviutem)
    }
}