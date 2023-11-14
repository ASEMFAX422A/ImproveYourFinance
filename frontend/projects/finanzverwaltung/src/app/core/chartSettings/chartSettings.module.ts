import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {NgIf} from '@angular/common';

import {
 ChartComponent,
 ApexAxisChartSeries,
 ApexChart,
 ApexXAxis,
 ApexDataLabels,
 ApexTitleSubtitle,
 ApexStroke,
 ApexGrid
} from "ng-apexcharts";
import { FormControl, FormGroup } from '@angular/forms';

export type ChartSettings = {
 lineChart: ApexChart;
 donutChartType: ApexChart;
 xaxis: ApexXAxis;
 lineChartGrid: ApexGrid;
 lineStroke: ApexStroke;
 responsive: ApexResponsive[];
};
@NgModule({

 imports: [CommonModule,],
 exports:[]
})
export class ChartSettingsModule { 
 public settings: Partial<ChartSettings>;
constructor(){
 this.settings = {
  lineChart: {
    height: 280,
    type: "line",
    zoom: {
      enabled: false
    }
  },
  lineStroke: {
    curve: "smooth"
  },
  lineChartGrid: {
    row: {
      opacity: 0.5
    }
  },
  responsive: [
    {
      breakpoint: 480,
      options: {
        chart: {
          width: 200
        },
        legend: {
          position: "bottom"
        }
      }
    }

  ],
  donutChartType: {
    type: "donut"

  },
}
}
}

