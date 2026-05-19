package university.ui;

import university.enums.Language;

import java.util.HashMap;
import java.util.Map;

public final class SwitchingLanguage {

    private static final SwitchingLanguage INSTANCE = new SwitchingLanguage();

    private Language currentLanguage = Language.EN;
    private final Map<Language, Map<String, String>> translations = new HashMap<>();

    private SwitchingLanguage() {
        loadDefaultMessages();
    }

    public static SwitchingLanguage getInstance() {
        return INSTANCE;
    }

    public void setLanguage(Language language) {
        if (language != null) {
            currentLanguage = language;
        }
    }

    public Language getLanguage() {
        return currentLanguage;
    }

    public String getText(String key, String s) {
        Map<String, String> map = translations.get(currentLanguage);
        if (map == null) {
            return "[" + key + "]";
        }
        return map.getOrDefault(key, "[" + key + "]");
    }

    private void put(Language lang, String key, String value) {
        translations.computeIfAbsent(lang, k -> new HashMap<>()).put(key, value);
    }

    private void loadDefaultMessages() {
        translations.put(Language.EN, new HashMap<>());
        translations.put(Language.RU, new HashMap<>());
        translations.put(Language.KZ, new HashMap<>());

        // ═══════════════════ ENGLISH ═══════════════════
        // Main menu
        put(Language.EN, "menu.title",           "KBTU University Management System");
        put(Language.EN, "menu.login",           "Login");
        put(Language.EN, "menu.register",        "Register Student");
        put(Language.EN, "menu.change_language", "Change Language");
        put(Language.EN, "menu.exit",            "Exit");
        put(Language.EN, "prompt.choice",        "Choice: ");
        put(Language.EN, "prompt.email",         "Email: ");
        put(Language.EN, "prompt.password",      "Password: ");
        put(Language.EN, "prompt.first_name",    "First name: ");
        put(Language.EN, "prompt.last_name",     "Last name: ");
        put(Language.EN, "prompt.language",       "Available: EN, RU, KZ");
        put(Language.EN, "prompt.select",         "Select: ");
        put(Language.EN, "message.goodbye",      "Goodbye!");
        put(Language.EN, "message.welcome",      "Welcome, ");
        put(Language.EN, "message.registered",   "Registered: ");
        put(Language.EN, "message.reg_failed",   "Registration failed (email exists or invalid).");
        put(Language.EN, "message.lang_changed", "Language changed.");
        put(Language.EN, "error.invalid",        "Invalid choice.");
        put(Language.EN, "error.credentials",    "Invalid credentials.");
        put(Language.EN, "error.language",        "Invalid language.");

        // Student menu
        put(Language.EN, "student.menu.title",       "STUDENT MENU: ");
        put(Language.EN, "student.transcript",       "View transcript");
        put(Language.EN, "student.marks",            "View marks");
        put(Language.EN, "student.register_course",  "Register for course");
        put(Language.EN, "student.view_courses",     "View available courses");
        put(Language.EN, "student.rate_teacher",     "Rate a teacher");
        put(Language.EN, "student.view_news",        "View news");
        put(Language.EN, "student.subscribe",        "Subscribe to journal");
        put(Language.EN, "student.info",             "My info");
        put(Language.EN, "student.tech_request",     "Submit tech request");
        put(Language.EN, "menu.logout",              "Logout");

        // Graduate student menu
        put(Language.EN, "grad.menu.title",         "GRADUATE STUDENT MENU: ");
        put(Language.EN, "grad.papers",             "My research papers");
        put(Language.EN, "grad.add_paper",          "Add research paper");
        put(Language.EN, "grad.projects",           "View research projects");
        put(Language.EN, "grad.hindex",             "H-index");
        put(Language.EN, "grad.supervisor",         "Assign supervisor");

        // Teacher menu
        put(Language.EN, "teacher.menu.title",       "TEACHER MENU: ");
        put(Language.EN, "teacher.courses",          "View my courses");
        put(Language.EN, "teacher.put_mark",         "Put mark");
        put(Language.EN, "teacher.complaint",        "Send complaint");
        put(Language.EN, "teacher.schedule",         "View schedule");
        put(Language.EN, "teacher.send_message",     "Send message to employee");
        put(Language.EN, "teacher.research",         "Research papers");
        put(Language.EN, "teacher.subscribe",        "Subscribe to journal");
        put(Language.EN, "teacher.inbox",            "View inbox");

        // Manager menu
        put(Language.EN, "manager.menu.title",       "MANAGER MENU: ");
        put(Language.EN, "manager.assign_course",    "Assign course to teacher");
        put(Language.EN, "manager.approve_reg",      "Approve student registration");
        put(Language.EN, "manager.add_course",       "Add course for registration");
        put(Language.EN, "manager.report",           "Academic report");
        put(Language.EN, "manager.news",             "Manage news");
        put(Language.EN, "manager.gpa",              "View students by GPA");
        put(Language.EN, "manager.alpha",            "View students alphabetically");
        put(Language.EN, "manager.courses",          "View all courses");
        put(Language.EN, "manager.schedule",         "View schedule");
        put(Language.EN, "manager.add_lesson",       "Add lesson to schedule");
        put(Language.EN, "manager.journals",         "View all journals");
        put(Language.EN, "manager.create_news",      "Create news");
        put(Language.EN, "manager.complaints",       "View complaints");

        // Admin menu
        put(Language.EN, "admin.menu.title",         "ADMIN MENU: ");
        put(Language.EN, "admin.view_users",         "View all users");
        put(Language.EN, "admin.add_user",           "Add user");
        put(Language.EN, "admin.remove_user",        "Remove user");
        put(Language.EN, "admin.logs",               "View system logs");
        put(Language.EN, "admin.courses",            "View all courses");
        put(Language.EN, "admin.remove_course",      "Remove course");
        put(Language.EN, "admin.journals",           "View all journals");
        put(Language.EN, "admin.inbox",              "View inbox");
        put(Language.EN, "admin.send_message",       "Send message");
        put(Language.EN, "admin.news",               "View news");

        // Tech support menu
        put(Language.EN, "tech.menu.title",          "TECH SUPPORT: ");
        put(Language.EN, "tech.new_requests",        "View new requests");
        put(Language.EN, "tech.accept",              "Accept request");
        put(Language.EN, "tech.reject",              "Reject request");
        put(Language.EN, "tech.done",                "Mark done");
        put(Language.EN, "tech.dashboard",           "Dashboard");

        // ═══════════════════ RUSSIAN ═══════════════════
        put(Language.RU, "menu.title",           "Система управления университетом КБТУ");
        put(Language.RU, "menu.login",           "Войти");
        put(Language.RU, "menu.register",        "Регистрация студента");
        put(Language.RU, "menu.change_language", "Сменить язык");
        put(Language.RU, "menu.exit",            "Выход");
        put(Language.RU, "prompt.choice",        "Выбор: ");
        put(Language.RU, "prompt.email",         "Почта: ");
        put(Language.RU, "prompt.password",      "Пароль: ");
        put(Language.RU, "prompt.first_name",    "Имя: ");
        put(Language.RU, "prompt.last_name",     "Фамилия: ");
        put(Language.RU, "prompt.language",       "Доступные: EN, RU, KZ");
        put(Language.RU, "prompt.select",         "Выберите: ");
        put(Language.RU, "message.goodbye",      "До свидания!");
        put(Language.RU, "message.welcome",      "Добро пожаловать, ");
        put(Language.RU, "message.registered",   "Зарегистрирован: ");
        put(Language.RU, "message.reg_failed",   "Регистрация не удалась (email уже существует или неверен).");
        put(Language.RU, "message.lang_changed", "Язык изменён.");
        put(Language.RU, "error.invalid",        "Неверный выбор.");
        put(Language.RU, "error.credentials",    "Неверные учетные данные.");
        put(Language.RU, "error.language",        "Неверный язык.");

        put(Language.RU, "student.menu.title",       "МЕНЮ СТУДЕНТА: ");
        put(Language.RU, "student.transcript",       "Просмотр транскрипта");
        put(Language.RU, "student.marks",            "Просмотр оценок");
        put(Language.RU, "student.register_course",  "Записаться на курс");
        put(Language.RU, "student.view_courses",     "Доступные курсы");
        put(Language.RU, "student.rate_teacher",     "Оценить преподавателя");
        put(Language.RU, "student.view_news",        "Просмотр новостей");
        put(Language.RU, "student.subscribe",        "Подписка на журнал");
        put(Language.RU, "student.info",             "Мои данные");
        put(Language.RU, "student.tech_request",     "Подать тех. заявку");
        put(Language.RU, "menu.logout",              "Выйти");

        put(Language.RU, "grad.menu.title",         "МЕНЮ МАГИСТРАНТА: ");
        put(Language.RU, "grad.papers",             "Мои научные работы");
        put(Language.RU, "grad.add_paper",          "Добавить научную работу");
        put(Language.RU, "grad.projects",           "Просмотр научных проектов");
        put(Language.RU, "grad.hindex",             "H-индекс");
        put(Language.RU, "grad.supervisor",         "Назначить руководителя");

        put(Language.RU, "teacher.menu.title",       "МЕНЮ ПРЕПОДАВАТЕЛЯ: ");
        put(Language.RU, "teacher.courses",          "Мои курсы");
        put(Language.RU, "teacher.put_mark",         "Поставить оценку");
        put(Language.RU, "teacher.complaint",        "Подать жалобу");
        put(Language.RU, "teacher.schedule",         "Расписание");
        put(Language.RU, "teacher.send_message",     "Отправить сообщение");
        put(Language.RU, "teacher.research",         "Научные работы");
        put(Language.RU, "teacher.subscribe",        "Подписка на журнал");
        put(Language.RU, "teacher.inbox",            "Входящие");

        put(Language.RU, "manager.menu.title",       "МЕНЮ МЕНЕДЖЕРА: ");
        put(Language.RU, "manager.assign_course",    "Назначить курс преподавателю");
        put(Language.RU, "manager.approve_reg",      "Одобрить регистрацию студента");
        put(Language.RU, "manager.add_course",       "Добавить курс для регистрации");
        put(Language.RU, "manager.report",           "Академический отчёт");
        put(Language.RU, "manager.news",             "Управление новостями");
        put(Language.RU, "manager.gpa",              "Студенты по GPA");
        put(Language.RU, "manager.alpha",            "Студенты по алфавиту");
        put(Language.RU, "manager.courses",          "Все курсы");
        put(Language.RU, "manager.schedule",         "Расписание");
        put(Language.RU, "manager.add_lesson",       "Добавить урок в расписание");
        put(Language.RU, "manager.journals",         "Все журналы");
        put(Language.RU, "manager.create_news",      "Создать новость");
        put(Language.RU, "manager.complaints",       "Просмотр жалоб");

        put(Language.RU, "admin.menu.title",         "МЕНЮ АДМИНА: ");
        put(Language.RU, "admin.view_users",         "Все пользователи");
        put(Language.RU, "admin.add_user",           "Добавить пользователя");
        put(Language.RU, "admin.remove_user",        "Удалить пользователя");
        put(Language.RU, "admin.logs",               "Системные логи");
        put(Language.RU, "admin.courses",            "Все курсы");
        put(Language.RU, "admin.remove_course",      "Удалить курс");
        put(Language.RU, "admin.journals",           "Все журналы");
        put(Language.RU, "admin.inbox",              "Входящие");
        put(Language.RU, "admin.send_message",       "Отправить сообщение");
        put(Language.RU, "admin.news",               "Просмотр новостей");

        put(Language.RU, "tech.menu.title",          "ТЕХ. ПОДДЕРЖКА: ");
        put(Language.RU, "tech.new_requests",        "Новые заявки");
        put(Language.RU, "tech.accept",              "Принять заявку");
        put(Language.RU, "tech.reject",              "Отклонить заявку");
        put(Language.RU, "tech.done",                "Отметить выполненной");
        put(Language.RU, "tech.dashboard",           "Панель управления");

        // ═══════════════════ KAZAKH ═══════════════════
        put(Language.KZ, "menu.title",           "КБТУ Университет Басқару Жүйесі");
        put(Language.KZ, "menu.login",           "Кіру");
        put(Language.KZ, "menu.register",        "Студент тіркеу");
        put(Language.KZ, "menu.change_language", "Тілді өзгерту");
        put(Language.KZ, "menu.exit",            "Шығу");
        put(Language.KZ, "prompt.choice",        "Таңдау: ");
        put(Language.KZ, "prompt.email",         "Пошта: ");
        put(Language.KZ, "prompt.password",      "Құпиясөз: ");
        put(Language.KZ, "prompt.first_name",    "Аты: ");
        put(Language.KZ, "prompt.last_name",     "Тегі: ");
        put(Language.KZ, "prompt.language",       "Қолжетімді: EN, RU, KZ");
        put(Language.KZ, "prompt.select",         "Таңдаңыз: ");
        put(Language.KZ, "message.goodbye",      "Сау болыңыз!");
        put(Language.KZ, "message.welcome",      "Қош келдіңіз, ");
        put(Language.KZ, "message.registered",   "Тіркелді: ");
        put(Language.KZ, "message.reg_failed",   "Тіркелу сәтсіз (email бар немесе қате).");
        put(Language.KZ, "message.lang_changed", "Тіл өзгертілді.");
        put(Language.KZ, "error.invalid",        "Қате таңдау.");
        put(Language.KZ, "error.credentials",    "Қате логин немесе құпиясөз.");
        put(Language.KZ, "error.language",        "Қате тіл.");

        put(Language.KZ, "student.menu.title",       "СТУДЕНТ МӘЗІРІ: ");
        put(Language.KZ, "student.transcript",       "Транскриптті көру");
        put(Language.KZ, "student.marks",            "Бағаларды көру");
        put(Language.KZ, "student.register_course",  "Курсқа тіркелу");
        put(Language.KZ, "student.view_courses",     "Қолжетімді курстар");
        put(Language.KZ, "student.rate_teacher",     "Оқытушыны бағалау");
        put(Language.KZ, "student.view_news",        "Жаңалықтарды көру");
        put(Language.KZ, "student.subscribe",        "Журналға жазылу");
        put(Language.KZ, "student.info",             "Mentің мәліметтерім");
        put(Language.KZ, "student.tech_request",     "Тех. сұрау жіберу");
        put(Language.KZ, "menu.logout",              "Шығу");

        put(Language.KZ, "grad.menu.title",         "МАГИСТРАНТ МӘЗІРІ: ");
        put(Language.KZ, "grad.papers",             "Менің ғылыми жұмыстарым");
        put(Language.KZ, "grad.add_paper",          "Ғылыми жұмыс қосу");
        put(Language.KZ, "grad.projects",           "Ғылыми жобаларды көру");
        put(Language.KZ, "grad.hindex",             "H-индекс");
        put(Language.KZ, "grad.supervisor",         "Жетекші тағайындау");

        put(Language.KZ, "teacher.menu.title",       "ОҚЫТУШЫ МӘЗІРІ: ");
        put(Language.KZ, "teacher.courses",          "Менің курстарым");
        put(Language.KZ, "teacher.put_mark",         "Баға қою");
        put(Language.KZ, "teacher.complaint",        "Шағым жіберу");
        put(Language.KZ, "teacher.schedule",         "Кесте");
        put(Language.KZ, "teacher.send_message",     "Хабарлама жіберу");
        put(Language.KZ, "teacher.research",         "Ғылыми жұмыстар");
        put(Language.KZ, "teacher.subscribe",        "Журналға жазылу");
        put(Language.KZ, "teacher.inbox",            "Кіріс хабарламалар");

        put(Language.KZ, "manager.menu.title",       "МЕНЕДЖЕР МӘЗІРІ: ");
        put(Language.KZ, "manager.assign_course",    "Курсты оқытушыға тағайындау");
        put(Language.KZ, "manager.approve_reg",      "Студент тіркеуін бекіту");
        put(Language.KZ, "manager.add_course",       "Тіркелу үшін курс қосу");
        put(Language.KZ, "manager.report",           "Академиялық есеп");
        put(Language.KZ, "manager.news",             "Жаңалықтарды басқару");
        put(Language.KZ, "manager.gpa",              "Студенттер GPA бойынша");
        put(Language.KZ, "manager.alpha",            "Студенттер әліпби бойынша");
        put(Language.KZ, "manager.courses",          "Барлық курстар");
        put(Language.KZ, "manager.schedule",         "Кесте");
        put(Language.KZ, "manager.add_lesson",       "Кестеге сабақ қосу");
        put(Language.KZ, "manager.journals",         "Барлық журналдар");
        put(Language.KZ, "manager.create_news",      "Жаңалық жасау");
        put(Language.KZ, "manager.complaints",       "Шағымдарды көру");

        put(Language.KZ, "admin.menu.title",         "АДМИН МӘЗІРІ: ");
        put(Language.KZ, "admin.view_users",         "Барлық пайдаланушылар");
        put(Language.KZ, "admin.add_user",           "Пайдаланушы қосу");
        put(Language.KZ, "admin.remove_user",        "Пайдаланушыны жою");
        put(Language.KZ, "admin.logs",               "Жүйе логтары");
        put(Language.KZ, "admin.courses",            "Барлық курстар");
        put(Language.KZ, "admin.remove_course",      "Курсты жою");
        put(Language.KZ, "admin.journals",           "Барлық журналдар");
        put(Language.KZ, "admin.inbox",              "Кіріс хабарламалар");
        put(Language.KZ, "admin.send_message",       "Хабарлама жіберу");
        put(Language.KZ, "admin.news",               "Жаңалықтарды көру");

        put(Language.KZ, "tech.menu.title",          "ТЕХ. ҚОЛДАУ: ");
        put(Language.KZ, "tech.new_requests",        "Жаңа сұраулар");
        put(Language.KZ, "tech.accept",              "Сұрауды қабылдау");
        put(Language.KZ, "tech.reject",              "Сұрауды қабылдамау");
        put(Language.KZ, "tech.done",                "Орындалды деп белгілеу");
        put(Language.KZ, "tech.dashboard",           "Басқару панелі");
    }
}