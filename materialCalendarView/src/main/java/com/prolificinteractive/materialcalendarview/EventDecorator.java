package com.prolificinteractive.materialcalendarview;

import java.util.ArrayList;
import java.util.Collection;

public class EventDecorator implements DayViewDecorator {
    private ArrayList<CalendarDay> dates;
    private int color;

    public EventDecorator(int color, Collection<CalendarDay> datesCollection) {
        dates = new ArrayList<>(datesCollection);
        this.color = color;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        if (view != null) {
            view.addSpan(new LineSpan(color));
        }
    }
}
