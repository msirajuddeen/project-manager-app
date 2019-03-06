import { Component, OnInit } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { ProjectManagerService } from '../../../shared/project-manager-service';
declare var jQuery:any;

@Component({
  selector: 'app-view-task',
  templateUrl: './view-task.component.html'
})
export class ViewTaskComponent implements OnInit {
    taskList: any = [];
    projectName: string = '';
    projectId: any = 0;
    inputParam: any = {};
    order: number = 1;  
    fieldName: string = '';
    projectList: any = [];
    selectedProject: string;
    displayError: boolean = false;
    screenLoader: boolean;
    errorReason:string ="Please check back-end connectivity!!!";
    constructor(public projectManagerService: ProjectManagerService, public router: Router, private datepipe: DatePipe) {
    }

    ngOnInit() {
        this.screenLoader = true;
        this.projectId = 0;
        this.projectName = '';
        this.taskList = [];
        this.inputParam = {};
        this.getProjectDetails();
    }
    getProjectDetails() {
        this.projectManagerService.getProject().subscribe(
          (data: any) => {
              if(null != data && undefined != data) {
                  this.projectList = data.projectVO;
                  this.screenLoader = false;
              } else {
                  this.screenLoader = false;
                  this.displayError = true;
              }
          },
          (err: any) => {
              this.screenLoader = false;
              this.displayError = true;
          }    
        );
    }

    getTask() {
        this.screenLoader = true;
        this.inputParam = {
            "taskVO" : {
                "projectId" : this.projectId
            }
        };
        this.projectManagerService.viewTask(this.inputParam).subscribe(
          (data: any) => {
              if(null != data && undefined != data) {
                  this.taskList = data.taskVO;
                  this.screenLoader = false;
              } else {
                  this.screenLoader = false;
                  this.displayError = true;
              }
          },
          (err: any) => {
              this.screenLoader = false;
              this.displayError = true;
          }    
        );        
    }

    setProject(proj: any) {
        this.projectName = proj.project;
        this.projectId = proj.projectId;
        jQuery('#searchProjectModalWindow').modal("hide");
        this.getTask();
    }

    editTask(task: any) {
        this.projectManagerService.task = task;
        this.router.navigate(['/editTask']);
    }

    endTask(task: any) {
        this.screenLoader = true;
        this.inputParam = {
            "taskVO" : {
                "taskId" : task.taskId,
                "task" : task.task,
                "parentId" : task.parentId,
                "projectId" : task.projectId,
                "priority" : task.priority,
                "startDate" : this.datepipe.transform(task.startDate, 'yyyy-MM-dd'),
                "endDate" : this.datepipe.transform(new Date(), 'yyyy-MM-dd'),
                "status" : "COMPLETED"
            }
        };
        this.projectManagerService.updateTask(this.inputParam).subscribe(
            (data: any) => {
                if(null != data && undefined != data && null !== data.status && undefined !== data.status && 'Success' === data.status) {
                    this.router.navigate(['/viewTask']);
                    this.screenLoader = false;
                } else {
                    this.screenLoader = false;
                    this.displayError = true;
                }
            },
            (err: any) => {
                this.screenLoader = false;
                this.displayError = true;
            }    
        );
        this.getTask();
    }

    getProject() {
        jQuery("#searchProjectPopupOpener").click();
    }

    sortTask(prop : any) {
        this.order = this.order * (-1);
        let order_val = this.order == 1 ? 'asc' : 'desc';
        this.fieldName = prop + "-" + order_val;
        return false;
    }
}
