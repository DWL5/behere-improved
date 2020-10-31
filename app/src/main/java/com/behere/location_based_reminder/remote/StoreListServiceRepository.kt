package com.behere.loc_based_reminder.data.remote

import android.util.Log
import com.behere.loc_based_reminder.data.response.Item
import com.behere.location_based_reminder.remote.StoreListServiceRemoteDataSource


class StoreListServiceRepository(
    private val storeListServiceRemoteDataSource:
    StoreListServiceRemoteDataSource,
) {

    private fun getAllStoreListNearBy(
        radius: Int, cx: Float, cy: Float, numOfRows: Int,
        success: (List<Item>) -> Unit,
        fail: (String) -> Unit
    ) {
        Log.d("API", "getAllStoreListNearBy() called.")
        storeListServiceRemoteDataSource.getStoreList(
            radius,
            cx,
            cy,
            numOfRows,
            pageNo = 1,
            success = success,
            fail = fail
        )
    }

    fun getToDoStoreListNearBy(
        radius: Int,
        cx: Float,
        cy: Float,
        numOfRows: Int,
        success: (HashMap<String, ArrayList<Item>>) -> Unit,
        fail: (String) -> Unit,
        vararg queries: String
    ) {
        Log.d("API", "getToDoStoreListNearBy() called.")
        val list = ArrayList<Item>()
        getAllStoreListNearBy(radius, cx, cy, numOfRows, success = {
            val map = HashMap<String, ArrayList<Item>>()
            for (item in it) {
                Log.d("API", "item $item")
                for (q in queries) {
                    Log.d("API", "query $q")
                    if (item.bizesNm.contains(q)) {
                        if (!map.containsKey(q)) {
                            map[q] = ArrayList<Item>()
                        }
                        map[q]?.add(item)
                    }
                }
            }
            success(map)
        }, fail = {
            fail(it)
        })
    }

    fun getToDoStoreListNearByString(
        radius: Int,
        cx: Float,
        cy: Float,
        numOfRows: Int
    ) {
        storeListServiceRemoteDataSource.getStoreListString(radius, cx, cy, numOfRows, 1)
    }
}