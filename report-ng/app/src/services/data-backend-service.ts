import {autoinject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-fetch-client';
import {data} from "services/report-model";
import ExecutionContext = data.ExecutionContext;
import SuiteContext = data.SuiteContext;
import TestContext = data.TestContext;
import ClassContext = data.ClassContext;
import MethodContext = data.MethodContext;


/**
 * Data backend service for retrieving and caching data backend responses
 * @author Mike Reiche <mike.reiche@t-systems.com>
 */
@autoinject()
export class DataBackendService {

  constructor(
    private _httpClient: HttpClient,
  ) {
    //  this.httpClient.configure(config => {
    //  config.withBaseUrl(TapConfig.DataBackendBaseUrl);
    //  });
    this._httpClient.configure(config => {
      config
        .useStandardConfiguration()
        .rejectErrorResponses()
        .withDefaults({
          "headers": {
            // "content-type": "application/octet-stream",
            "accept": "*/*"
          }
        });
    });
  }

  protected get(path: string): Promise<Response> {
    return this._httpClient.fetch(path, {
      method: "GET",
    });
  }

  protected responseToProtobufJSMessage(response: Response, messageClass) {
    return response.arrayBuffer().then(buffer => {
      return messageClass.decode(new Uint8Array(buffer))
    });
  }

  getExecution(): Promise<ExecutionContext> {
    return this.get("model/execution")
      .then(response => {
        return this.responseToProtobufJSMessage(response, ExecutionContext)
      })
  }


  getSuite(apath : string): Promise<SuiteContext> {
    return this.get("model/suites/"+apath)
      .then(response => {
        return this.responseToProtobufJSMessage(response, SuiteContext)
      })
  }

  // @ts-ignore
  getTest(apath : string): Promise<TestContext> {
    return this.get("model/tests/"+apath)
      .then(response => {
        return this.responseToProtobufJSMessage(response, TestContext)
      })
  }

  // @ts-ignore
  getClass(apath : string): Promise<ClassContext> {
    return this.get("model/classes/"+apath)
      .then(response => {
        return this.responseToProtobufJSMessage(response, ClassContext)
      })
  }

  // @ts-ignore
  getMethod(apath : string): Promise<MethodContext> {
    return this.get("model/methods/"+apath)
      .then(response => {
        return this.responseToProtobufJSMessage(response, MethodContext)
      })
  }
}
