<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="parameter">
    Include Patterns:<strong><props:displayValue name="org.kkanojia.tasks.teamcity.include" emptyValue="none"/></strong>
</div>

<div class="parameter">
    Exclude Patterns:<strong><props:displayValue name="org.kkanojia.tasks.teamcity.exclude" emptyValue="none"/></strong>
</div>

<div class="parameter">
    Minor level:<strong><props:displayValue name="org.kkanojia.tasks.teamcity.minor" emptyValue="none"/></strong>
</div>

<div class="parameter">
    Major level:<strong><props:displayValue name="org.kkanojia.tasks.teamcity.major" emptyValue="none"/></strong>
</div>

<div class="parameter">
    Critical level:<strong><props:displayValue name="org.kkanojia.tasks.teamcity.critical" emptyValue="none"/></strong>
</div>
