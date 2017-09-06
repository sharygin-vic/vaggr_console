# vaggr_console
<b>Vacancy aggregator.</b>

Based on task <a href=https://javarush.ru/quests/lectures/questcollections.level08.lecture15>Aggregator</a> of the JavaRush courses.
That was my <a href=https://github.com/sharygin-vic/JavaRushTasks/tree/84650a8aa2f851a38079ce81aa06cde9a8b01397/4.JavaCollections/src/com/javarush/task/task28/task2810>solution</a>.

<b>Patterns:</b>
MVC, strategy, factory method.

<b>Technologies:</b>
JavaSE, jsoup, HTML, CSS.

<b>Description:</b>
Application is intended to collect vacancies according to assigned locations and job keywords.
It looks for on the next sites: 
<ul>
  <li>hh.ua</li><li>jobs.dou.ua</li><li>moikrug.ru</li><li>neuvoo.com.ua</li><li>rabota.ua</li><li>trud.com</li><li>ua.indeed.com</li><li>ua.jooble.org</li><li>work.ua.
</ul>
<b>Particular qualities:</b>
Console application.
There is a provision to adjust search parameters in properties files.
Application writes results into folder "html_result" and opens them in the web browser window.

## Screenshot
![Screenshot](screenshots/1.jpg?raw=true "Search results")
