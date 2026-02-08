package com.example.gamemanager

object AppCommons
{
    private lateinit var currentProject: Project

    public fun getCurrentProject() = currentProject

    public fun setCurrentProject(p: Project) { currentProject = p }
}