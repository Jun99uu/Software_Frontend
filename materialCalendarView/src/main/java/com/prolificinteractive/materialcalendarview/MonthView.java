package com.prolificinteractive.materialcalendarview;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

/**
 * Display a month of {@linkplain DayView}s and
 * seven {@linkplain WeekDayView}s.
 */
@SuppressLint("ViewConstructor") class MonthView extends CalendarPagerView {

  public MonthView(
      @NonNull final MaterialCalendarView view,
      final CalendarDay month,
      final DayOfWeek firstDayOfWeek,
      final boolean showWeekDays) {
    super(view, month, firstDayOfWeek, showWeekDays);
  }

  @Override protected void buildDayViews(
      final Collection<DayView> dayViews,
      final LocalDate calendar) {
    LocalDate temp = calendar;
    for (int r = 0; r < DEFAULT_MAX_WEEKS; r++) {
      for (int i = 0; i < DEFAULT_DAYS_IN_WEEK; i++) {
        addDayView(dayViews, temp);
        temp = temp.plusDays(1);
      }
    }
    for (DayView dayView : dayViews) {
      dayView.setOnDragListener(new OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent e) {
//        ImageView imageView = (ImageView)v;
          switch (e.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
              if (e.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

//              imageView.setColorFilter(Color.BLUE);
                v.invalidate();
                return true;
              } else {
                return false;
              }
            case DragEvent.ACTION_DRAG_ENTERED:
//            imageView.setColorFilter(Color.GREEN);
              v.invalidate();
              return true;
            case DragEvent.ACTION_DRAG_LOCATION:
              return true;
            case DragEvent.ACTION_DRAG_EXITED:
//            imageView.setColorFilter(Color.BLUE);
              v.invalidate();
              return true;
            case DragEvent.ACTION_DROP:
              ClipData.Item item = e.getClipData().getItemAt(0);
              CharSequence dragData = item.getText();
              Log.d("Drop to", dayView.getDate().toString());
              //TODO: 핸드폰 내부저장소에 플랜 저장
//              String filename = "plan";
//              StringBuffer buff = new StringBuffer(dayView.getDate().toString());
//              String content = buff.substring(12,buff.length()-1);
//              try(FileOutputStream fos = getContext().openFileOutput(filename, Context.MODE_PRIVATE)){
//                fos.write(content.getBytes());
//              }catch(Exception exception){
//                exception.getMessage();
//              }
//              dayView.getRootView().invalidate();
              DayView.tmp = 1;
              Toast.makeText(getContext(), "Drag data is " + dragData, Toast.LENGTH_SHORT).show();
              v.invalidate();
              return true;
            case DragEvent.ACTION_DRAG_ENDED:
//            imageView.clearColorFilter();
              v.invalidate();
              if (e.getResult()) {
//                Log.d("Drop ended", "handled");
              } else {
                Log.d("Drop ended", "didn't work");
              }
              return true;
            default:
              Log.e("DragDrop Example", "Unknown action type");
              return false;
          }
        }
      });
    }
  }

  public CalendarDay getMonth() {
    return getFirstViewDay();
  }

  @Override protected boolean isDayEnabled(final CalendarDay day) {
    return day.getMonth() == getFirstViewDay().getMonth();
  }

  @Override protected int getRows() {
    return showWeekDays ? DEFAULT_MAX_WEEKS + DAY_NAMES_ROW : DEFAULT_MAX_WEEKS;
  }
}
