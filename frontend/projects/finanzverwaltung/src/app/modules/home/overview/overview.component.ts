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
  ApexGrid,
  ApexPlotOptions,
  ApexYAxis,
  ApexFill
} from "ng-apexcharts";
import { ChartSettingsModule } from '../../../core/chartSettings/chartSettings.module';
import { RequestService } from '../../../core/services/request.service';


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
@Component({
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})

export class OverviewComponent {

  @ViewChild("chart") chart!: ChartComponent;
  public chartData: Partial<chartData>;
  public chartOptions: Partial<ChartOptions>;
  
  username = this.auth.getUsername();
  startBalance=0;
  endBalance=0;

  getStartDate() {
    let startDate = new Date();
    startDate.setMonth(startDate.getMonth() - 1);
    startDate.setDate(1);
    return startDate;
  }

  getEndDate() {
    let endDate = new Date();
    endDate.setDate(0);
    return endDate;
  }

  loadData() {
    this.requestService.post("bank-account/overview", {
      "id": "all",
      "start": 1694124000000, //TODO: startDategetTime(),
      "end": this.getEndDate().getTime(),
    }).subscribe(response => {
      this.updateData(response);

    });
  }
  
  updateData(data: any) {
    var balance = data.startBalance;
    this.startBalance = data.startBalance;
    this.endBalance = data.endBalance;
    const startDate = this.getStartDate();
    console.log(data);
    console.log(data.dailyExpenses.sort((a:any,b:any)=>new Date(a).getTime()- new Date(b).getTime()));
    console.log(data);
    
    this.chartData.title = { text: "Einnahmen/Ausgaben Übersicht für: " + startDate.toLocaleString('de-DE', { month: 'long' }) };
    this.chartOptions.title = {
      text: "Einnahmen/Ausgaben Übersicht für: " + startDate.toLocaleString('de-DE', { month: 'long' }),
      floating: false,
      offsetY: 20,
      align: "center",
      style: {
        color: "#444"
      }
    }
    this.chartData.donut_data= data.categoryExpenses.map((category:any)=>{
     let categoryAmount:number = category.amount.toFixed(0);
     if(categoryAmount < 0){
      return Math.round(category.amount * -1 *100)/100;
     }
     console.log(categoryAmount);
     return Math.round(category.amount *100)/100;
    });

    this.chartData.donut_labels= data.categoryExpenses.map((category:any)=>{
      var categoryName = category.category?.name;
      if(categoryName == null){
        return "Nicht Kategorisiert";
      }
      return categoryName;
    });
    
    this.chartData.series =
      [
        {
          name: "Einnahmen/Ausgaben",
          data: data.dailyExpenses.map((transaction: any) => {
            balance +=transaction.amount;
            return balance.toFixed(2);
          }),

        }
      ],
      this.chartOptions.series =
      [
        {
          name: "Einnahmen/Ausgaben",
          data: data.dailyExpenses.map((transaction: any) => transaction.amount.toFixed(2)),
        }
      ];
      this.chartOptions.xaxis={
        categories: data.dailyExpenses.map((transaction:any) =>new Date(transaction.date).getDate().toString()+"."),
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
      };
      this.chartData.xaxis={
        categories: data.dailyExpenses.map((transaction:any) =>new Date(transaction.date).getDate().toString()+"."),
      }


  }
  
  constructor(protected chartSettings: ChartSettingsModule, private requestService: RequestService, private auth: AuthService) {
    this.chartData ={};
    this.loadData();


    this.chartOptions = {
      series: [
        {
          name: "Inflation",
          data: [2.3, 3.1, 4.0, 200, 4.0, 3.6, 3.2, 2.3, 1.4, 0.8, 0.5, -222]
        }
      ],
      chart: {
        height: 280,
        type: "bar",
        zoom: {
          enabled: false
        }
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
        offsetY: -20,
        style: {
          fontSize: "12px",
          colors: ["#212121"]
        }
      },
      fill: {
        colors: [function ({ value, seriesIndex, w }: { value: number, seriesIndex: number, w: any }) {
          if (value < 0) {
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

        }
      },

    };
  }
}
