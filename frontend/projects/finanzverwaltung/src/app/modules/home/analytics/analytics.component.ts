import { AuthService } from '../../../core/services/auth.service';
import { RegisterComponent } from '../../auth/register/register.component';
import { LoginComponent } from '../../auth/login/login.component';
import { OverviewComponent } from '../overview/overview.component';
import { Component, ViewChild } from "@angular/core";
import * as ApexCharts from 'apexcharts';
import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexDataLabels,
  ApexTitleSubtitle,
  ApexStroke,
  ApexGrid,
  ApexPlotOptions,
  ApexYAxis,
  ApexFill
} from "ng-apexcharts";
import { FormControl, FormGroup } from '@angular/forms';
import { ChartSettingsModule } from '../../../core/chartSettings/chartSettings.module';


export type chartData = {
  series: ApexAxisChartSeries;
  donut_data: ApexNonAxisChartSeries;
  xaxis: ApexXAxis;
  grid: ApexGrid;
  title: ApexTitleSubtitle;
  donut_labels: any;

};
export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  plotOptions: ApexPlotOptions;
  yaxis: ApexYAxis;
  xaxis: ApexXAxis;
  fill: ApexFill;
  title: ApexTitleSubtitle;
};
const today = new Date();
const month = today.getMonth();
const year = today.getFullYear();

@Component({
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.scss']
})

export class AnalyticsComponent {
  campaignOne = new FormGroup({
    start: new FormControl(new Date(year, month, today.getDate())),
    end: new FormControl(new Date(year, month,today.getDate()+1)),
  });
  getDataDate(){
    
    const startDate = this.campaignOne.value;
    console.log(startDate);
    console.log(startDate.start?.getTime());
    console.log(startDate.end?.getTime());
    return alert("Ok");
  }
  //username= this.auth.getUsername();


  @ViewChild("chart") chart!: ChartComponent;

  public chartData: Partial<chartData>;
  public chartOptions: Partial<ChartOptions>;





  constructor(public chartSettings: ChartSettingsModule) {


    this.chartData = {
      series: [
        {
          name: "Ausgaben",
          data: [1, 2, 3, 4, 55],
        }
      ],
      xaxis: {
        categories: [1, 2, 3, 5, 7],

      },
      donut_data: [44],


      donut_labels: ["Team A", "Team B", "Team C", "Team D", "Team E"],

      title: {
        text: "Ausgabe Übersicht für: " + "",

      },

    };
    this.chartOptions = {
      series: [
        {
          name: "Inflation",
          data: [2.3, 3.1, 4.0, 200, 4.0, 3.6, 3.2, 2.3, 1.4, 0.8, 0.5, -222]
        }
      ],
      chart: {
        height: 280,
        type: "bar"
      },
      plotOptions: {
        bar: {
          dataLabels: {
            position: "top"
          }
        }
      },
      dataLabels: {
        enabled: true,
        formatter: function(val) {
          return val + "%";
        },
        offsetY: -20,
        style: {
          fontSize: "12px",
          colors: ["#212121"]
        }
      },

      xaxis: {
        categories: [
          "Jan",
          "Feb",
          "Mar",
          "Apr",
          "May",
          "Jun",
          "Jul",
          "Aug",
          "Sep",
          "Oct",
          "Nov",
          "Dec"
        ],
        position: "top",
        labels: {
          offsetY: -18
        },
        axisBorder: {
          show: false
        },
        axisTicks: {
          show: false
        },
        crosshairs: {
          fill: {
            type: "gradient",
            gradient: {
              colorFrom: "#D8E3F0",
              colorTo: "#BED1E6",
              stops: [0, 100],
              opacityFrom: 0.4,
              opacityTo: 0.5
            }
          }
        },
        tooltip: {
          enabled: true,
          offsetY: -35
        }
      },
      fill: {
        colors: [function({ value, seriesIndex, w }:{ value: number, seriesIndex: number, w: any }) {
          if(value < 0) {
              return '#fd0606'
          } else {
              return '#06fd1b'
          }
        }]
      },
      yaxis: {
        axisBorder: {
          show: false
        },
        axisTicks: {
          show: false
        },
        labels: {
          show: false,
          formatter: function(val) {
            return val + "%";
          }
        }
      },
      title: {
        text: "Monthly Inflation in Argentina, 2002",
        floating: false,
        offsetY: 320,
        align: "center",
        style: {
          color: "#444"
        }
      }
    };



  }
}
