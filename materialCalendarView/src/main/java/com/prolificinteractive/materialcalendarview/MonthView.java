package com.prolificinteractive.materialcalendarview;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.LineBackgroundSpan;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

/**
 * Display a month of {@linkplain DayView}s and
 * seven {@linkplain WeekDayView}s.
 */
@SuppressLint("ViewConstructor") public class MonthView extends CalendarPagerView {

  public static MaterialCalendarView materialCalendarView = null;
  public static int colorIndex = 0;
  public static int[] colors = {Color.BLUE,Color.YELLOW,Color.GREEN,Color.BLACK,Color.RED,Color.CYAN,Color.rgb(255,165,0)};
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
      dayView.setOnDragListener((v, e) -> {
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
            int days = dragData.charAt(0)-'0';
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
            ArrayList<CalendarDay> set = new ArrayList<>();
            CalendarDay tmp = dayView.getDate();
            for(int i=0; i<days; i++){
              set.add(tmp);
              tmp = addOnetoCalendarDay(tmp);
            }
            materialCalendarView.addDecorator(new EventDecorator(colors[colorIndex],set));
            colorIndex = (colorIndex+1)%colors.length;
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
      });
    }
  }
  private CalendarDay addOnetoCalendarDay(CalendarDay date){
//    val days = arrayListOf<Int>(0,31,28,31,30,31,30,31,31,30,31,30,31);
    int[] days = new int[]{0,31,28,31,30,31,30,31,31,30,31,30,31};


//    var day = date.day+1
//    var month = date.month
//    var year = date.year
    int day = date.getDay()+1;
    int month = date.getMonth();
    int year = date.getYear();
    if( year % 4 == 0 && ( year % 100 !=0 || year % 400 == 0)){
      days[2] = 29;
    }
    if(date.getDay() >= days[date.getMonth()]){
      day = 1;
      month = month+1;
      if(month > 12){
        month = 1;
        year = year+1;
      }

    }
    return CalendarDay.from(year,month,day);
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

class EventDecorator implements DayViewDecorator{
  private ArrayList<CalendarDay> dates;
  private int color;
  EventDecorator(int color, Collection<CalendarDay> datesCollection){
    dates = new ArrayList<>(datesCollection);
    this.color = color;
  }

  @Override
  public boolean shouldDecorate(CalendarDay day) {
    return dates.contains(day);
  }

  @Override
  public void decorate(DayViewFacade view) {
    if(view!=null){
      view.addSpan(new LineSpan(color));
    }
  }
}

class LineSpan implements LineBackgroundSpan{

  private int color;
  LineSpan(int color){
    this.color = color;
  }
  @Override
  public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence charSequence, int start, int end, int lineNum) {
    Rect rect = new Rect();
    rect.set(left,top-(top-bottom), right, bottom - (top-bottom));
    paint.setColor(color);
    canvas.drawRect(rect,paint);
  }
}