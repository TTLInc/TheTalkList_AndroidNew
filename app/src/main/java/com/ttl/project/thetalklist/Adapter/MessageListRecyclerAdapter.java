package com.ttl.project.thetalklist.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ttl.project.thetalklist.Bean.ChatroomModel;
import com.ttl.project.thetalklist.CircleTransform;
import com.ttl.project.thetalklist.FragmentStack;
import com.ttl.project.thetalklist.MessageList;
import com.ttl.project.thetalklist.MessageOneToOne;
import com.ttl.project.thetalklist.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Saubhagyam on 17/04/2017.
 */

public class MessageListRecyclerAdapter  extends RecyclerView.Adapter<MessageListRecyclerAdapter.MyViewHolder>{

    final Context context;
    List<ChatroomModel> chatroomModelList;
    FragmentManager fragmentManager;



    public MessageListRecyclerAdapter(Context context,List<ChatroomModel> chatroomModelList, FragmentManager fragmentManager) {
        this.context = context;
        this.chatroomModelList=chatroomModelList;
        this.fragmentManager=fragmentManager;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_message,parent,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final ChatroomModel chatroomModel = chatroomModelList.get(position);

        if (chatroomModel.getUnread()>0){
            holder.senderName.setTextColor(Color.parseColor("#000000"));
            holder.messagelist_indicator.setVisibility(View.VISIBLE);
        }

        holder.senderName.setText(chatroomModel.getSenderName());
        String picPath = chatroomModel.getSenderPic();

        String date=chatroomModel.getLastTime();
//        Date date1 = null;
        Date date_txt=null;
        String[] months={"Jan","Feb","Mar","April","may","June","July","Aug","Sep","Oct","Nov","Dec"};
        try {
            if (date!=null) {
                date_txt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(date);
                String newString = new SimpleDateFormat("HH:mm", Locale.US).format(date_txt); // 9:00
                String newdate = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(date_txt); // 9:00


                int hour= Integer.parseInt(new SimpleDateFormat("HH", Locale.US).format(date_txt));
                int month= Integer.parseInt(new SimpleDateFormat("MM", Locale.US).format(date_txt));
                int day= Integer.parseInt(new SimpleDateFormat("dd", Locale.US).format(date_txt));
                /*holder.Messageday.setText(newdate);
                holder.MessageTime.setText(newString);*/
                String newStr= new SimpleDateFormat("HH:mm", Locale.US).format(date_txt); // 9:00
                String h= new SimpleDateFormat("HH", Locale.US).format(date_txt); // 9:00
                String m= new SimpleDateFormat("mm", Locale.US).format(date_txt); // 9:00
                if (hour>12){
                    String time=months[month-1]+" "+String.valueOf(day);
                    holder.MessageTime.setText(String.valueOf(Integer.parseInt(h)-12)+":"+m+" pm");
                    holder.Messageday.setText(time);
                }else{
                    holder.MessageTime.setText(newStr+" am");
                    String time=months[month-1]+" "+String.valueOf(day);
                    holder.Messageday.setText(time);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }



        if (!picPath.equals("")) {
            Glide.with(context).load("https://www.thetalklist.com/uploads/images/" + picPath)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.SenderImg);
        } else {
            Glide.with(context).load("https://www.thetalklist.com/images/header.jpg")
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.SenderImg);

        }


        SharedPreferences chatPref=context.getSharedPreferences("chatPref",Context.MODE_PRIVATE);
        final SharedPreferences.Editor chatPrefEditor=chatPref.edit();
        holder.chatroom_list_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chatPrefEditor.putString("firstName",chatroomModel.getSenderName());
                chatPrefEditor.putInt("receiverId",chatroomModel.getSenderId()).apply();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                FragmentStack fragmentStack=FragmentStack.getInstance();
//                        fragmentStack.add(new MessageList());
                fragmentStack.push(new MessageList());
                MessageOneToOne messageOneToOne=new MessageOneToOne();
                fragmentTransaction.replace(R.id.viewpager, messageOneToOne).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatroomModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        final TextView senderName;
        final TextView Messageday;
        final TextView MessageTime;
        final ImageView SenderImg;
        final ImageView messagelist_indicator;
        final LinearLayout chatroom_list_layout;

        public MyViewHolder(View itemView) {
            super(itemView);


            senderName= (TextView) itemView.findViewById(R.id.senderName);
            Messageday= (TextView) itemView.findViewById(R.id.Messageday);
            MessageTime= (TextView) itemView.findViewById(R.id.Messagetime);
            SenderImg= (ImageView) itemView.findViewById(R.id.SenderImg);
            messagelist_indicator= (ImageView) itemView.findViewById(R.id.messagelist_indicator);

            chatroom_list_layout= (LinearLayout) itemView.findViewById(R.id.chatroom_list_layout);
        }
    }

}
