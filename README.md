Тестируемое API - https://beta.todoist.com/
Документация API - https://developer.todoist.com/rest/v1/
1. Сделать тест дизайн для метода Create a new task. Можно в виде чек листов.
2. Автоматизировать минимум 3 из созданных тестов.
3. Желательно использовать java junit/testng.
<hr>
В данный момент реализовано - 7 тест кейсов. <br>
Использован JUnit 5. <br>
Библиотеки: Jackson, Lombok. <br>
<b>AuthorizationTest</b> проверяет возможность авторизации с предустановленным токеном.<br>
<b>NewTaskTest</b> имеет два тест метода - первый метод <b>newTaskIsPresentInActiveTasks</b> который проверяет создание новой задачи с параметрами по умолчанию и проверяет то, находится ли эта задача в списке активных.</br>
Второй метод <b>allPositiveTestCasesShouldBePassed</b> является параметризованным тест кейсом, проверяющим что каждый из позитивных тест кейсов указанных в CSV файле возвращает валидно созданную задачу.<br>
Это тестовое задание, поэтому не является production-like. При доработке было бы учтено: <ul>
<li>Созданные задачи не удаляются в AfterAll методе.</li>
<li>При проверке позитивных тест кейсов в TaskTestsPositiveCases.csv мы всегда ожидаем 200 OK. Негативные тест кейсы были бы дополнены полем expectedStatusCode с ожидаемым кодом ошибки и полем expectedErrorMessage с ожидаемым сообщением об ошибке. </li>
<li>Были бы проверены контекстно-зависимые переменные, такие как parent или section_id</li>
</ul>  
<hr>
Тест кейсы для метода Create a new task:<br>
<ol>
<li>Проверка content. По максимальной длине, пустой строке, null, спецсимволам, кодировке, отсутствию поля в JSON, дефолтного значения.</li>
<li>Проверка project_id. Проверка дефолтного значения (отсутствия значения), валидного значения, невалидного значения по типу, невалидного значения, id проекта в неактивном статусе. </li>
<li>Проверка section_id. Проверка дефолтного значения, валидного значения секции, невалидного значения секции, значение секции в невалидном состоянии. </li>
<li>Проверка order. Проверка на отрицательность значения, 0, на дефолтное значение, на значение выходящее за пределы int'а. </li>
<li>Проверка label_ids. Отсутствие значение, невалидное по типу, каждый элемент массива также как и content</li>
<li>Проверка priority. Валидных значения только 4 - от 1 до 4 включительно. Невалидные значения по типу, по значению.</li>
<li>Проверка due_string. Аналогично content.</li>
<li>Проверка due_date. Проверка соответствия ожидаемому формату (YYYY-MM-DD). Проверка прошлого значения, проверка невалидного значения по смыслы (3000+ год).</li>
<li>Проверка due_datetime. Аналогично due_date.</li>
<li>Проверка due_lang. Проверка типа, количества символов, валидность самого кода.</li>
</ol>