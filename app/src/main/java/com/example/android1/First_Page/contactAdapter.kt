package com.example.android1

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_a.view.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.io.InputStream
import java.nio.channels.AsynchronousFileChannel.open
import java.nio.channels.Pipe.open
import java.util.*


class contactAdapter(val JsonList:ArrayList<list_item>): RecyclerView.Adapter<contactAdapter.ViewHolder>() /*,Filterable*/ {

    private var filterList: ArrayList<list_item> = JsonList

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.findViewById<TextView>(R.id.tv_name)
        var number = itemView.findViewById<TextView>(R.id.tv_number)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): contactAdapter.ViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return contactAdapter.ViewHolder(inflatedView).apply {

            /*Toast message*/
            /*itemView.setOnLongClickListener(object :View.OnLongClickListener{
                    override fun onLongClick(v: View?): Boolean {
                        val curPos: Int = adapterPosition
                        var item: list_item = JsonList.get(curPos)

                        //JsonList.removeAt(curPos)
                        Toast.makeText(parent.context,
                            "${curPos}\n ${item.name}\n ${item.number}",
                            Toast.LENGTH_SHORT
                        ).show()
                        return true
                    }

                })
*/
            /* call */
            itemView.iv_call.setOnClickListener {
                val curPos: Int = adapterPosition
                var item: list_item = JsonList.get(curPos)
                val intent_call = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.number))
                parent.context.startActivity(intent_call)
            }

            /* message */
            itemView.iv_mms.setOnClickListener {
                val curPos: Int = adapterPosition
                var item: list_item = JsonList.get(curPos)
                val intent_mms = Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + item.number))
                parent.context.startActivity(intent_mms)
            }

            /*delete*/
            itemView.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(view: View?): Boolean {
                    val curPos: Int = adapterPosition
                    //var item: list_item = JsonList.get(curPos)
                    //다이얼로그 생성
                    var builder = AlertDialog.Builder(view?.context)
                    val inflater = LayoutInflater.from(view?.context)
                    val dialogView = inflater.inflate(R.layout.custom_dialog, null)
                    val dialogText = dialogView.findViewById<TextView>(R.id.dg_content)
                    builder.setView(dialogView)
                        .setPositiveButton("확인") { dialogInterface, i ->
                            builder.setTitle(dialogText.text.toString())
                            //remove
                            JsonList.remove(JsonList.get(curPos))

                            notifyItemRemoved(curPos)
                            Log.d("JsonList", JsonList.toString())
                            Log.d("JsonList_size", JsonList.size.toString())
                            notifyItemRangeChanged(curPos,JsonList.size)
                            Log.d("JsonList", JsonList.toString())
                            Log.d("JsonList_size", JsonList.size.toString())
                            //Toast.makeText(view?.context, "파일이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("취소") { dialogInterface, i ->
                        }
                        .show()
                    return true
                }
            })

            /*add*/
            itemView.btn_add.setOnClickListener (object : View.OnClickListener {
                override fun onClick(view: View?) {
                    Log.d("111111", "clicked")
                    val curPos: Int = adapterPosition
                    //var item: list_item = JsonList.get(curPos)
                    //다이얼로그 생성
                    var add_builder = AlertDialog.Builder(view?.context)
                    val inflater = LayoutInflater.from(view?.context)
                    val dialogView = inflater.inflate(R.layout.cutom_add_dialog, null)
                    Log.d("dia", dialogView.toString())
                    val dialogText = dialogView.findViewById<TextView>(R.id.dg_title)
                    val id: String = ""
                    val dialogName = dialogView.findViewById<TextView>(R.id.addName_)
                    val dialogNumber = dialogView.findViewById<TextView>(R.id.addNumber_)

                    add_builder.setView(dialogView)
                        .setPositiveButton("확인") { dialogInterface, i ->
                            add_builder.setTitle(dialogText.text.toString())
                            JsonList.add(list_item(id, dialogName.toString(), dialogNumber.toString()))
                            notifyItemRemoved(curPos)
                            notifyItemRangeChanged(curPos,JsonList.size)
                        }
                        .setNegativeButton("취소") { dialogInterface, i ->
                        }
                        .show()

                }
            })


        }
            }


            /*edit*/


    override fun getItemCount(): Int {
        return this.JsonList.size
    }

    override fun onBindViewHolder(holder: contactAdapter.ViewHolder, position: Int) { //class ViewHolder을 연결
        holder.name.setText((JsonList[position].name))
        holder.number.text = (JsonList[position].number)
    }

}


