package com.example.gamemanager

open class CharacterDecorator(private val component: Character): Character
{
    // GETTERS
    public override fun getOwnerProject():               Project         = component.getOwnerProject()
    public override fun getName():                       String          = component.getName()
    public override fun getAliases():                    String          = component.getAliases()
    public override fun getSpecies():                    String          = component.getSpecies()
    public override fun getBirth():                      String          = component.getBirth()
    public override fun getAge():                        String          = component.getAge()
    public override fun getAspect():                     String          = component.getAspect()
    public override fun getPersonality():                String          = component.getPersonality()
    public override fun getBackstory():                  List<String>    = component.getBackstory()
    public override fun getBStoryParagraph(index: Int):  String          = component.getBStoryParagraph(index)
    public override fun getBStoryParagraphCount():       Int             = component.getBStoryParagraphCount()

    // SETTERS AND MODIFIER METHODS
    public override fun setName(newName: String)                     { component.setName(newName) }
    public override fun setAliases(newAliases: String)               { component.setAliases(newAliases) }
    public override fun setSpecies(newSpecies: String)               { component.setSpecies(newSpecies) }
    public override fun setBirth(newBirth: String)                   { component.setBirth(newBirth) }
    public override fun setAge(newAge: String)                       { component.setAge(newAge) }
    public override fun setAspect(newAspect: String)                 { component.setAspect(newAspect) }
    public override fun setPersonality(newPers: String)              { component.setPersonality(newPers) }
    public override fun setBStoryParagraph(index: Int, par: String)  { component.setBStoryParagraph(index, par) }
    public override fun addBStoryParagraph(par: String)              { component.addBStoryParagraph(par) }
    public override fun removeBStoryParagraph(index: Int)            { component.removeBStoryParagraph(index) }
}