import { AuthService } from '../core/services/auth.service';
import { RegisterComponent } from '../modules/auth/register/register.component';
import { LoginComponent } from '../modules/auth/login/login.component';

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
  ApexGrid
} from "ng-apexcharts";
export type ChartOptions = {
  series: ApexAxisChartSeries;
  seriess: ApexNonAxisChartSeries;
  chart: ApexChart;
  chartt: ApexChart;
  xaxis: ApexXAxis;
  dataLabels: ApexDataLabels;
  grid: ApexGrid;
  stroke: ApexStroke;
  title: ApexTitleSubtitle;
  responsive: ApexResponsive[];
  labels: any;
};

@Component({
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})

export class OverviewComponent {
  //username= this.auth.getUsername();

  @ViewChild("chart") chart!: ChartComponent;
  public chartOptions: Partial<ChartOptions>;
  test(){
    this.chartOptions.title ={
        text: "Test",
        align: "left"
        
      };
    this.chartOptions.series=[{
      name: "Laptops",
      data:[10, 1, 33, 51, 49, 62, 1, 3, 0, 10, 41, 35, 1, 1, 62, 7, 91, 148],
    }];
      console.log("Hallo");
  }
  constructor() {
    this.chartOptions = {
      series: [
        {
          name: "Desktops",
          data: [10, 41, 35, 51, 49, 62, 69, 91, 0, 10, 41, 35, 51, 49, 62, 69, 91, 148],
        }
      ],
      seriess: [44, 55, 13, 43, 22],
      
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
      labels: ["Team A", "Team B", "Team C", "Team D", "Team E"],
      chart: {
        height: 280,
        type: "line",
        zoom: {
          enabled: false
        }
      },
      chartt: {
          type:"donut"
        
      },
      dataLabels: {
        enabled: false
      },
      stroke: {
        curve: "straight"
      },
      title: {
        text: "Product Trends by Month",
        align: "left"
      },
      grid: {
        row: {
          colors: ["#f3f3f3", "transparent"], // takes an array which will be repeated on columns
          opacity: 0.5
        }
      },
      xaxis: {
        categories: [
          "1",
          "2",
          "3",
          "4",
          "5",
          "6",
          "7",
          "8",
          "9",
          "1",
          "2",
          "3",
          "4",
          "5",
          "6",
          "7",
          "8",
          "9"
        ]
      }
    };
  }
}
