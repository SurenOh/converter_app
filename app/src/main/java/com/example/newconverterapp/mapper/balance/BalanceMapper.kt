package com.example.newconverterapp.mapper.balance

import com.example.newconverterapp.db.entity.BalanceEntity
import com.example.newconverterapp.model.BalanceModel
import com.example.newconverterapp.util.Mapper

class BalanceMapper : Mapper<BalanceEntity, BalanceModel> {

    override fun mapToModel(t: BalanceEntity) = BalanceModel(
        balance = t.balance,
        code = t.code
    )

    fun mapFromModel(model: BalanceModel) = BalanceEntity(
        balance = model.balance,
        code = model.code
    )

}