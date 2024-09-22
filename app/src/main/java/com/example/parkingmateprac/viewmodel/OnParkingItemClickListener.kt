// OnParkingItemClickListener.kt
package com.example.parkingmateprac.viewmodel

import com.example.parkingmateprac.model.entity.ParkingItemEntity

interface OnParkingItemClickListener {
    fun onParkingItemClick(parkingItem: ParkingItemEntity)
}
