# CustomeRecycleViewDemo
# Step 1. Add the JitPack repository to your build file

# Add it in your root build.gradle at the end of repositories:
# allprojects 
	{
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
 # Step 2. Add the dependency
  
 # dependencies 
 	{
	        implementation 'com.github.imobdevtechbhavin:CustomeRecycleViewDemo:1.0'
		implementation 'androidx.recyclerview:recyclerview:1.1.0'
	}
  
  
# .xml 
 	  <com.chart.recycleview.custome.RecycleViewCustom
            android:id="@+id/customView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:is_load_more="true"
            app:is_swipe_refresh_layout="true"
            app:rv_span_count="2"
            app:set_layout_manager="LinearLayoutManager" />
	    
# activty.kt
	
	class MainActivity : AppCompatActivity(),  RecycleViewCustom.onSwipeToRefresh,
    RecycleViewCustom.onLoadMore, BaseBindingAdapter.ItemClickListener<Int?> {
    override fun onItemClick(view: View, data: Int?, position: Int) {
        Toast.makeText(this@MainActivity,"",Toast.LENGTH_LONG).show()
    }

    override fun onLoadMore() {
        Log.e("LOADMORE","--------------->")
        Handler().postDelayed(Runnable {
            (binding.customView.rvItems.adapter as TestAdepter).removeFooterProgressItem(binding.customView)
            (binding.customView.rvItems.adapter as TestAdepter).addItems(testingList)

        },3000)

    }
    override fun onSwipeToRefresh() {
        Log.e("SWIPE_TO_REFRESH","--------------->")

        setData()
    }


    /*binding variable for this activity*/
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    var testingList:ArrayList<Int?> = ArrayList()

    private fun init() {
        GetAccountsName()
        val adepter = TestAdepter()
        binding.customView.setAdepter(adepter)
        binding.customView.swipeToRefreshItemClick=this
        binding.customView.onLoadMoreItemClick=this
        adepter.itemClickListener=this
        setData()
    }


    private fun setData() {
        binding.customView.setTotalPages(3)
        (binding.customView.rvItems.adapter as TestAdepter).clear()
        testingList.add(R.drawable.a)
        testingList.add(R.drawable.b)
        testingList.add(R.drawable.c)
        testingList.add(R.drawable.d)
        testingList.add(R.drawable.e)
        testingList.add(R.drawable.f)
        testingList.add(R.drawable.g)
        testingList.add(R.drawable.h)


        (binding.customView.rvItems.adapter as TestAdepter).setItem(testingList)
        (binding.customView.rvItems.adapter as TestAdepter).notifyItemRangeInserted(0,testingList.size)

    }
# Adepter 
	class TestAdepter : BaseBindingAdapter<Int?>() {

    override fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
        return ItemListTestBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_ITEM -> {

                val binding: ItemListTestBinding = holder.binding as ItemListTestBinding
                val item = items[position]
                item?.let {
                    binding.tv.text=""+item
                    Glide
                        .with(binding.root.context)
                        .load(item)
                        .into(binding.iv)

                }
            }

        }

    }
	}
