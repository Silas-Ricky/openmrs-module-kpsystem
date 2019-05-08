<%
	config.require("encounters")

	if (config.encounters && config.encounters.size() > 0) {
		config.encounters.each { encounter ->
			def title = ui.format(encounter.form ?: encounter.encounterType)

			title += " (" + kenyaui.formatDateAuto(encounter.encounterDatetime) + ")"

			def providers = encounter.providersByRoles.values().collectAll { ui.format(it) }.flatten().join(", ")

			def form = encounter.form ? ui.simplifyObject(encounter.form) : [ iconProvider : "kenyaemr", icon : "forms/generic.png" ]

			def onClick = config.onEncounterClick instanceof Closure ? config.onEncounterClick(encounter) : config.onEncounterClick
%>
<div class="ke-stack-item ke-navigable" onclick="${ onClick }">
	<input type="hidden" name="encounterId" value="${ encounter.encounterId }"/>
	<i class="fa fa-plus-square fa-2x"></i>
	<b>${ title }</b> by ${ providers }<br/>
</div>
<%
		}
	} else {
%>
<i>None</i>
<% } %>