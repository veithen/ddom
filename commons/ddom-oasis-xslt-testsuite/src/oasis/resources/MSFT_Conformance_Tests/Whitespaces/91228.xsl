<?xml version="1.0"?> 
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" omit-xml-declaration="yes" indent="no"/>

<xsl:template match="//riches/book">
	<xsl:copy>
	  <xsl:apply-templates select="@*"/>
	</xsl:copy>
</xsl:template>

</xsl:stylesheet>