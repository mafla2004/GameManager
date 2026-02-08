package com.example.gamemanager

object AppCommons
{
    private var currentProject: Project? = null

    public fun getCurrentProject() = currentProject

    public fun setCurrentProject(p: Project) { currentProject = p }
}