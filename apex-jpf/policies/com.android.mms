<Policies TargetUid="10029">
<Policy Effect="Permit">
<Permission Name = "android.permission.SEND_SMS" />
<Constraint CombiningAlgorithm="org.csrdu.apex.combiningalgorithms.All">
	<Expression FunctionId="org.csrdu.apex.functions.DateEqual">
	<ApplicationAttribute AttributeName="sentSms" default="3"/>
	<ApplicationAttribute AttributeName="lastUsedDay" default="eval(day(System.CurrentDate)- 1)" />
	<SharedAttribute SharedAtt="remSms" default="3"/>
	<ApplicationAttribute AttributeName="totalSms" default="5"/>
	<SystemAttribute AttributeName="CurrentDate" default="0" />
	<Constant Value = "1"/>
	</Expression>
</Constraint>
</Policy> 
</Policies>