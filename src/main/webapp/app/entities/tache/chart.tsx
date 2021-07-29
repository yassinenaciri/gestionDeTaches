import React, { Component, useEffect } from 'react';
import { Doughnut } from 'react-chartjs-2';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IStats } from 'app/shared/model/stats';
import { getStats, getStatsForEmploye } from 'app/entities/tache/tache.reducer';

export const Chart = props => {
  const dispatch = useAppDispatch();
  const stats: IStats = useAppSelector(state => state.tache.stats);

  useEffect(() => {
    if (props && props.id) {
      dispatch(getStatsForEmploye(props.id));
    } else {
      dispatch(getStats());
    }
  }, []);

  return (
    <Doughnut
      data={stats}
      options={{
        responsive: true,
        maintainAspectRatio: true,
      }}
    />
  );
};
