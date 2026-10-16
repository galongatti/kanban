//package br.com.kanban.kanbanback.entity;
//
//
//import jakarta.persistence.*;
//
//@Entity
//public class UserBoardAccess {
//
//    @Id
//    @GeneratedValue(strategy =  GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_board_access_user"))
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "board_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_board_access_board"))
//    private Board board;
//
//
//}
