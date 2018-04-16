import { Http, RequestOptions, Headers, URLSearchParams } from '@angular/http';
import { Injectable } from '@angular/core';

// import 'rxjs/add/operator/map';
// import 'rxjs/add/operator/toPromise';
// import { Http, RequestOptions, Headers, URLSearchParams } from '@angular/http';
/*
  Generated class for the ApiServiceProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class ApiServiceProvider {
  apiUrl = "http://localhost:8011/COMP4601-Project/rest/project"
  constructor(public http: Http) {
    console.log('Hello ApiServiceProvider Provider');
  }

  // Get All Available skills
  getAllSkills(): Promise<any> {
    var me = this;

    return me.http.get(me.apiUrl + "/skills").toPromise()
      .then((res: any) => {
        console.log("response from API call to /skills:")
        console.log(res)

        // Convert the response body to json and return
        let body = res.json()
        return body
      })
      .catch(error => {

        console.log("error returned from /skills api call: ")
        console.log(error)
        return error
      });
  }

  // Get Relevant skills
  getRelevantSkills(skill: string): Promise<any> {
    var me = this;
    console.log("getting RelevantSkills for skill: " + skill)
    return me.http.get(me.apiUrl + "/db/" + skill).toPromise()
      .then((res: any) => {
        console.log("response from API call to /db/" + skill + ":")
        console.log(res)
        // Convert the response body to json and return
        let body = res.json()
        return body
      })
      .catch(error => {

        console.log("error from API call to /db/" + skill + ":")
        console.log(error)
        return error
      });
  }

}
