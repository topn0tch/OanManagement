package com.oan.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String firstName;
    private String lastName;
    @Email
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String password;
    private int bugsReported;
    private int tasksMade;
    private int tasksCompleted;
    private int tasksReceived;
    private int tasksAssigned;
    private int messagesReceived;
    private int messagesSent;
    private int eventsCreated;
    private Date registrationDate;
    private String country;
    private int age;
    private boolean motivationalTaskMessage = true;
    private boolean smallCalendar = true;
    private boolean todoToCalendar = true;
    private boolean showEmail = true;
    // Social
    private String skype;
    private String twitter;
    private String github;
    private String facebook;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @OneToMany
    private List<Task> tasks;

    @OneToMany
    private List<Contact> contacts;

    @OneToMany
    private List<Message> messages;

    @OneToMany
    private List<Event> events;

    @OneToMany
    private List<Bug> bugs;

    @OneToOne
    private Rank rank;

    public User() {
    }

    public User(String username, String firstName, String lastName, String email, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(String username, String firstName, String lastName, String email, String password, Collection<Role> roles) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public int getTasksReceived() {
        return tasksReceived;
    }

    public void setTasksReceived(int tasksReceived) {
        this.tasksReceived = tasksReceived;
    }

    public int getTasksAssigned() {
        return tasksAssigned;
    }

    public void setTasksAssigned(int tasksAssigned) {
        this.tasksAssigned = tasksAssigned;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public boolean isShowEmail() {
        return showEmail;
    }

    public void setShowEmail(boolean showEmail) {
        this.showEmail = showEmail;
    }

    public boolean isTodoToCalendar() {
        return todoToCalendar;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setTodoToCalendar(boolean todoToCalendar) {
        this.todoToCalendar = todoToCalendar;
    }

    public boolean isSmallCalendar() {
        return smallCalendar;
    }

    public void setSmallCalendar(boolean smallCalendar) {
        this.smallCalendar = smallCalendar;
    }

    public boolean isMotivationalTaskMessage() {
        return motivationalTaskMessage;
    }

    public void setMotivationalTaskMessage(boolean motivationalTaskMessage) {
        this.motivationalTaskMessage = motivationalTaskMessage;
    }

    public String getCountry() {
        return country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getEventsCreated() {
        return eventsCreated;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setEventsCreated(int eventsCreated) {
        this.eventsCreated = eventsCreated;
    }

    public int getMessagesReceived() {
        return messagesReceived;
    }

    public void setMessagesReceived(int messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    public int getMessagesSent() {
        return messagesSent;
    }

    public void setMessagesSent(int messagesSent) {
        this.messagesSent = messagesSent;
    }

    public int getTasksMade() {
        return tasksMade;
    }

    public void setTasksMade(int tasksMade) {
        this.tasksMade = tasksMade;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }

    public void setTasksCompleted(int tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    public int getBugsReported() {
        return bugsReported;
    }

    public void setBugsReported(int bugsReported) {
        this.bugsReported = bugsReported;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username'" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + "*********" + '\'' +
                ", roles=" + roles +
                '}';
    }
}
