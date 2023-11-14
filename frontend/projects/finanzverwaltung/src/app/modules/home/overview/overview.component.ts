import { AuthService } from '../../../core/services/auth.service';
import { RegisterComponent } from '../../auth/register/register.component';
import { LoginComponent } from '../../auth/login/login.component';

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
import { ChartSettingsModule } from '../../../core/chartSettings/chartSettings.module';


export type chartData = {
  series: ApexAxisChartSeries;
  donut_data: ApexNonAxisChartSeries;
  xaxis: ApexXAxis;
  grid: ApexGrid;
  title: ApexTitleSubtitle;
  donut_labels: any;

};


@Component({
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
}) 

export class OverviewComponent {

  //username= this.auth.getUsername();


  @ViewChild("chart") chart!: ChartComponent;
  public chartData: Partial<chartData>;






  constructor(protected chartSettings: ChartSettingsModule) {

    
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
        text: "Ausgabe Übersicht für: "+"",

      },

    };


    
  }
}
