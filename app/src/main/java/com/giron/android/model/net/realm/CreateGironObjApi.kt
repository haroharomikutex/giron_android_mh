@file:Suppress("KDocUnresolvedReference")

package com.giron.android.model.net.realm

import com.giron.android.model.dao.CreateGironObj
import com.giron.android.model.dao.CreateGironTagObj
import com.giron.android.model.entity.CreateGironEntity
import com.giron.android.model.entity.TagEditEntity
import com.giron.android.view.giron.create.viewModel.CreateGironViewModel
import io.realm.Realm
import io.realm.RealmList

/**
 * CreateGironObjApi
 */
class CreateGironObjApi : RealmObjApi() {
    /**
     * getData
     *
     * @return CreateGironObj?
     */
    fun getData() : CreateGironEntity {
        Realm.getDefaultInstance().use {
            val result = it.where(CreateGironObj::class.java).findFirst()

            result?.let {obj ->
                val tags = obj.tags.map { tag ->
                    return@map TagEditEntity(tag.name, 0, tag.isLock, 0)
                }

                return CreateGironEntity(obj.title, obj.description, tags)
            }
        }

        return CreateGironEntity("", "", arrayListOf())
    }

    /**
     * setData
     *
     * @param String title
     * @param String description
     * @param ArrayList<TagViewModel> tags
     */
    fun setData(model: CreateGironViewModel) {
        Realm.getDefaultInstance().use {realm ->
            realm.executeTransaction {
                // delete old
                realm.where(CreateGironObj::class.java).findAll().deleteAllFromRealm()
                realm.where(CreateGironTagObj::class.java).findAll().deleteAllFromRealm()

                // create tags
                val tags = RealmList<CreateGironTagObj>()
                model.tags.forEach {
                    val tag = CreateGironTagObj()
                    tag.name = it.name
                    tag.isLock = it.isLock.value ?: false
                    tags.add(tag)
                }

                // create giron
                val createGiron = CreateGironObj()
                createGiron.title = model.title.value ?: ""
                createGiron.description = model.description.value ?: ""
                createGiron.tags = tags

                realm.insertOrUpdate(createGiron)
            }
        }
    }

    /**
     * removeData
     */
    fun removeData() {
        Realm.getDefaultInstance().use {realm ->
            realm.executeTransaction {
                // delete old
                realm.where(CreateGironObj::class.java).findAll().deleteAllFromRealm()
                realm.where(CreateGironTagObj::class.java).findAll().deleteAllFromRealm()
            }
        }

    }
}