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
  ApexGrid
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
const today = new Date();
const month = today.getMonth();
console.log(month);
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
    return alert("Ok");
  }
  //username= this.auth.getUsername();


  @ViewChild("chart") chart!: ChartComponent;

  public chartData: Partial<chartData>;






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




  }
}
