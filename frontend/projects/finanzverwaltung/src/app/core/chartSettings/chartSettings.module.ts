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
 chart: ApexChart;
 xaxis: ApexXAxis;
 lineChartGrid: ApexGrid;
 lineStroke: ApexStroke;
 responsive: ApexResponsive[];
 responsiveLine: ApexResponsive[];
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
      breakpoint: 450,
      options: {
        chart: {
          width: 400,
        },
        chartt: {
          width: 300,
          height: 200,
        },

        legend: {
          position: "bottom"
        }
      }
    }
  ],
  responsiveLine: [
    {
      breakpoint: 400,
      options: {

        chart: {
          width: 270,
          height: 170
        },
      }
      
    },
    {
      breakpoint: 600,
      options: {

        chart: {
          width: 300,
          height: 200
        },
      }
      
    },
    {
      breakpoint: 780,
      options: {

        chart: {
          width: 460,
          height: 200,
        },
      }
      
    },
    {
      breakpoint: 800,
      options: {

        chart: {
          width: 600,
          height: 200,
        },
      }
      
    },
    {
      breakpoint: 850,
      options: {

        chart: {
          width: 600,
          height: 200,
        },
      }
      
    },
    {
      breakpoint: 900,
      options: {

        chart: {
          width: 600,
          height: 200,
        },
      }
      
    },
    {
      breakpoint: 1000,
      options: {

        chart: {
          width: 650,
          height: 200,
        },
      }
      
    },
    {
      breakpoint: 1100,
      options: {

        chart: {
          width: 700,
          height: 200,
        },
      }
      
    },
    {
      breakpoint: 1300,
      options: {

        chart: {
          width: 800,
          height: 200,
        },
      }
      
    },
    {
      breakpoint: 1400,
      options: {

        chart: {
          width: 900,
          height: 200,
        },
      }
      
    },
    {
      breakpoint: 1600,
      options: {

        chart: {
          height: 190,
        },
      }
      
    }

  ],
  chart: {
    type: "radialBar"

  },
}
}
}

