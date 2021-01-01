package com.example.android1

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_a.view.*
import kotlinx.android.synthetic.main.list_item.view.*


class contactAdapter(private val JsonList:ArrayList<list_item>):
        RecyclerView.Adapter<contactAdapter.ViewHolder>(){


    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        var name = itemView.findViewById<TextView>(R.id.tv_name)
        var number = itemView.findViewById<TextView>(R.id.tv_number)
        private var ck = 0
        var checkbox: CheckBox = itemView!!.findViewById<CheckBox>(R.id.checkBox)
        var checkBoxList = arrayListOf<checkboxData>()

        fun bind(data: list_item, num:Int){
            // var layoutParam = list_item.layoutParams as ViewGroup.MarginLayoutParams
            if(ck == 1){
                checkbox.visibility = View.VISIBLE
            }else
                checkbox.visibility = View.GONE
            if(num>=checkBoxList.size){
                checkBoxList.add(num, checkboxData(itemId, false))
            }
            checkbox.isChecked = checkBoxList[num].checked
            checkbox.setOnClickListener{
                if(checkbox.isChecked){
                    checkBoxList[num].checked = true
                }else{
                    checkBoxList[num].checked = false
                }
            }
            Log.d("checkbox",checkbox.toString())
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): contactAdapter.ViewHolder {
            val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return contactAdapter.ViewHolder(inflatedView).apply {
                /*Toast message*/
                itemView.setOnLongClickListener(object :View.OnLongClickListener{
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

                /* call */
                itemView.iv_call.setOnClickListener{
                    val curPos: Int = adapterPosition
                    var item: list_item = JsonList.get(curPos)
                    val intent_call = Intent(Intent.ACTION_CALL, Uri.parse("tel:"+item.number))
                    parent.context.startActivity(intent_call)
                }
                itemView.iv_mms.setOnClickListener{
                    val curPos: Int = adapterPosition
                    var item: list_item = JsonList.get(curPos)
                    val intent_mms = Intent(Intent.ACTION_VIEW,  Uri.parse("tel:" + item.number))
                    parent.context.startActivity(intent_mms)

                }

                //체크 박스 클릭시 bool 값을 1로 줌
                //delete이벤트 발생 시 체크박스 리스트에서 1로 되어있는 아이템의 포지션을 받아와 delete 해줌


                /*delete
                itemView.checkBox.setOnClickListener(View.OnClickListener {
                    val curPos: Int = adapterPosition

                    var cb: CheckBox
                    itemView.isSelected(cb.isChecked);

                    val curPos: Int = adapterPosition
                    Toast.makeText(parent.context,
                            "checked ${curPos} position",
                            Toast.LENGTH_SHORT
                    ).show()
                })*/

            }
        }

    override fun getItemCount(): Int {
       return JsonList.size
    }

    override fun onBindViewHolder(holder: contactAdapter.ViewHolder, position: Int) { //class ViewHolder을 연결

        holder.name.setText((JsonList.get(position).name))
        holder.number.setText((JsonList.get(position).number))
        //holder.checkBox.setChecked();

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

}
class checkboxData(
        var id: Long,
        var checked: Boolean)