package com.marco.backend.todoapp.backend_todoapp.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marco.backend.todoapp.backend_todoapp.models.entities.PriorityEnum;
import com.marco.backend.todoapp.backend_todoapp.models.entities.Todo;
import com.marco.backend.todoapp.backend_todoapp.services.ITodoService;

import jakarta.validation.Valid;



@RestController
@CrossOrigin(origins = "*")
public class TodoController {
    @Autowired
    private ITodoService service;

    // http://localhost:8080/todos?done=false&page=1&name=pan&priority=Low
    @GetMapping("/todos")
    public Map<String, Object> getTodos(
        @RequestParam(required = false, defaultValue = "0") Integer page,
        @RequestParam(required = false) Boolean done,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) PriorityEnum priority,
        @RequestParam(required = false) List<String>  sortBy,
        @RequestParam(required = false) Sort.Direction sortDirection
    ) {
        return this.service.getTodosFiltered(done, name, priority, page, sortBy, sortDirection);
    }


    @GetMapping("/todos/metrics")
    public Map<String, Object> getMetrics() {
        return this.service.getMetrics();
    }

    @GetMapping("/health")
    public ResponseEntity<Void> healthCheck() {
        return new ResponseEntity<>(HttpStatus.OK);
    }


    //    retorna 
    //    {
    //     "averageTimeToFinishByPriority": {
    //         "Low": "0 days, 0 hours, 0 minutes, 0 seconds",
    //         "Medium": "0 days, 0 hours, 0 minutes, 0 seconds",
    //         "High": "3 days, 12 hours, 17 minutes, 24 seconds"
    //     },
    //     "averageEstimatedTimeToComplete": "0 days, 12 hours, 0 minutes, 0 seconds"
    // }

    

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getTodoById(@PathVariable String id) {
        Todo todo = service.findById(id);
        if (todo != null)
        {
            return ResponseEntity.ok(todo);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/todos")
    public ResponseEntity<?> create(@Valid @RequestBody Todo todo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(todo));
    }

    @PutMapping("todos/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable String id, @RequestBody Todo todo) {
        Todo todoUpdated = service.update(id, todo);
        if (todoUpdated != null)
        {
            return ResponseEntity.status(HttpStatus.OK).body(todoUpdated);
        }
        return ResponseEntity.notFound().build();
    }

    /*
     * Si todo esta bien me retorna: 
     * {
            "id": "2",
            "taskName": "Hacer la tareaEditado",
            "priority": "High",
            "dueDate": "2025-06-01T00:00:00",
            "done": false,
            "doneDate": "2025-04-14T00:15:12.077234",
            "creationDate": "2025-04-13T12:15:12.077229"
        }
     * Si no hace las validaciones por ejemplo, si mande dueDate incorrecto 
     * {
            "timestamp": "2025-04-13T18:15:40.049+00:00",
            "status": 400,
            "error": "Bad Request",
            "trace": "org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot deserialize value of type `java.time.LocalDateTime` from String \"2025-04-08T20:37:35.024+00:00\": Failed to deserialize java.time.LocalDateTime: (java.time.format.DateTimeParseException) Text '2025-04-08T20:37:35.024+00:00' could not be parsed, unparsed text found at index 23\n\tat org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.readJavaType(AbstractJackson2HttpMessageConverter.java:409)\n\tat org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.read(AbstractJackson2HttpMessageConverter.java:357)\n\tat org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver.readWithMessageConverters(AbstractMessageConverterMethodArgumentResolver.java:204)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor.readWithMessageConverters(RequestResponseBodyMethodProcessor.java:176)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor.resolveArgument(RequestResponseBodyMethodProcessor.java:150)\n\tat org.springframework.web.method.support.HandlerMethodArgumentResolverComposite.resolveArgument(HandlerMethodArgumentResolverComposite.java:122)\n\tat org.springframework.web.method.support.InvocableHandlerMethod.getMethodArgumentValues(InvocableHandlerMethod.java:227)\n\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:181)\n\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:118)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:986)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:891)\n\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)\n\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1089)\n\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:979)\n\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014)\n\tat org.springframework.web.servlet.FrameworkServlet.doPut(FrameworkServlet.java:925)\n\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:593)\n\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885)\n\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:195)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\n\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\n\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\n\tat org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\n\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)\n\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\n\tat org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)\n\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)\n\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167)\n\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90)\n\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:483)\n\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115)\n\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93)\n\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74)\n\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:344)\n\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:397)\n\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63)\n\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:905)\n\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1743)\n\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52)\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1190)\n\tat org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)\n\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:63)\n\tat java.base/java.lang.Thread.run(Thread.java:1583)\nCaused by: com.fasterxml.jackson.databind.exc.InvalidFormatException: Cannot deserialize value of type `java.time.LocalDateTime` from String \"2025-04-08T20:37:35.024+00:00\": Failed to deserialize java.time.LocalDateTime: (java.time.format.DateTimeParseException) Text '2025-04-08T20:37:35.024+00:00' could not be parsed, unparsed text found at index 23\n at [Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); line: 5, column: 16] (through reference chain: com.marco.backend.todoapp.backend_todoapp.models.entities.Todo[\"dueDate\"])\n\tat com.fasterxml.jackson.databind.exc.InvalidFormatException.from(InvalidFormatException.java:67)\n\tat com.fasterxml.jackson.databind.DeserializationContext.weirdStringException(DeserializationContext.java:1959)\n\tat com.fasterxml.jackson.databind.DeserializationContext.handleWeirdStringValue(DeserializationContext.java:1245)\n\tat com.fasterxml.jackson.datatype.jsr310.deser.JSR310DeserializerBase._handleDateTimeException(JSR310DeserializerBase.java:176)\n\tat com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer._fromString(LocalDateTimeDeserializer.java:216)\n\tat com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer.deserialize(LocalDateTimeDeserializer.java:114)\n\tat com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer.deserialize(LocalDateTimeDeserializer.java:41)\n\tat com.fasterxml.jackson.databind.deser.SettableBeanProperty.deserialize(SettableBeanProperty.java:543)\n\tat com.fasterxml.jackson.databind.deser.BeanDeserializer._deserializeWithErrorWrapping(BeanDeserializer.java:585)\n\tat com.fasterxml.jackson.databind.deser.BeanDeserializer._deserializeUsingPropertyBased(BeanDeserializer.java:447)\n\tat com.fasterxml.jackson.databind.deser.BeanDeserializerBase.deserializeFromObjectUsingNonDefault(BeanDeserializerBase.java:1497)\n\tat com.fasterxml.jackson.databind.deser.BeanDeserializer.deserializeFromObject(BeanDeserializer.java:348)\n\tat com.fasterxml.jackson.databind.deser.BeanDeserializer.deserialize(BeanDeserializer.java:185)\n\tat com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.readRootValue(DefaultDeserializationContext.java:342)\n\tat com.fasterxml.jackson.databind.ObjectReader._bindAndClose(ObjectReader.java:2131)\n\tat com.fasterxml.jackson.databind.ObjectReader.readValue(ObjectReader.java:1501)\n\tat org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.readJavaType(AbstractJackson2HttpMessageConverter.java:398)\n\t... 51 more\nCaused by: java.time.format.DateTimeParseException: Text '2025-04-08T20:37:35.024+00:00' could not be parsed, unparsed text found at index 23\n\tat java.base/java.time.format.DateTimeFormatter.parseResolved0(DateTimeFormatter.java:2111)\n\tat java.base/java.time.format.DateTimeFormatter.parse(DateTimeFormatter.java:2010)\n\tat java.base/java.time.LocalDateTime.parse(LocalDateTime.java:494)\n\tat com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer._fromString(LocalDateTimeDeserializer.java:214)\n\t... 63 more\n",
            "message": "JSON parse error: Cannot deserialize value of type `java.time.LocalDateTime` from String \"2025-04-08T20:37:35.024+00:00\": Failed to deserialize java.time.LocalDateTime: (java.time.format.DateTimeParseException) Text '2025-04-08T20:37:35.024+00:00' could not be parsed, unparsed text found at index 23",
            "path": "/todos/2"
        }
     */



    @PostMapping("todos/{id}/done")
    public ResponseEntity<?> updateDone(@Valid @PathVariable String id) {
        Todo todoUpdated = service.updateDone(id);
        if (todoUpdated != null)
            return ResponseEntity.status(HttpStatus.OK).body(service.save(todoUpdated));
        return ResponseEntity.notFound().build();
        
    }

     /*
     * Si todo esta bien cambia de "done": false, a done: true me retorna: tambien si ya tenia done retorna lo mismo mandando 200
     * {
            "id": "2",
            "taskName": "Hacer la tarea",
            "priority": "High",
            "dueDate": "2025-06-01T00:00:00",
            "done": true,
            "doneDate": "2025-04-13T12:38:13.112191",
            "creationDate": "2025-04-13T12:36:17.863083"
        }
     * Si hay un error y manda un id inexcsitente retorna 404 not found
     */




    @PutMapping("todos/{id}/undone")
    public ResponseEntity<?> updateUndone(@Valid @PathVariable String id) {
        Todo todoUpdated = service.updateUndone(id);
        if (todoUpdated != null)
        {
            return ResponseEntity.status(HttpStatus.OK).body(todoUpdated);
        }
        return ResponseEntity.notFound().build();
    }

     /*
     * Si todo esta bien me retorna: 
     * {
            "id": "2",
            "taskName": "Hacer la tareaEditado",
            "priority": "High",
            "dueDate": "2025-06-01T00:00:00",
            "done": false,
            "doneDate": "2025-04-14T00:15:12.077234",
            "creationDate": "2025-04-13T12:15:12.077229"
        }
     *Si hay un error y manda un id inexcsitente retorna 404 not found
     * 
     */

    @DeleteMapping("todos/{id}")
    public ResponseEntity<?> delete(@Valid @PathVariable String id) {
        Todo todo = service.findById(id);
        if (todo != null)
        {
            service.remove(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

     /*
     * Si todo esta bien me retorna: 
     * {
            "id": "2",
            "taskName": "Hacer la tarea",
            "priority": "High",
            "dueDate": "2025-06-01T00:00:00",
            "done": false,
            "doneDate": "2025-04-14T00:36:17.863089",
            "creationDate": "2025-04-13T12:36:17.863083"
        }

     *      *Si hay un error y manda un id inexcsitente retorna 404 not found


     */

}
