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
